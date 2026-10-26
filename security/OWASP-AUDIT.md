# OWASP Top 10 (2025) Audit — SOR

_Date: 2026-10-25 · Scope: whole repository · Commit: c268a6f · Tree: clean_

## System summary
SOR is a small Java 11 / Maven batch-processing prototype: a home-grown spout/bolt "topology" engine. Two entry programs exist:
`service.SOR` (generates synthetic order lists, slices them into orders and executions, persists executions to DynamoDB Local at `http://localhost:8000`) and
`service.CHFGVAPMerger` (reads fixed-width files of card numbers (PAN) + EIN/TIN and of EIN + first/last names from local disk, joins them through Solr at `http://localhost:8983`, and writes PAN + name records into a Solr core `pinccvpii`).
There is no network listener, no HTTP API and no user authentication. Trust boundaries are the input files, the Solr cores (read back and deserialized with Jackson) and stdout logs. The key assets are **cardholder data (PAN) linked to names and tax ids** — PCI DSS / PII scope.

## Summary
| Category | Status | Findings |
|---|---|---|
| A01 Broken Access Control | N/A | C:0 H:0 M:0 L:0 |
| A02 Security Misconfiguration | Issues | C:0 H:0 M:0 L:0 I:1 |
| A03 Software Supply Chain Failures | Issues | C:0 H:1 M:0 L:0 |
| A04 Cryptographic Failures | Issues | C:0 H:1 M:0 L:0 |
| A05 Injection | No issues found | C:0 H:0 M:0 L:0 |
| A06 Insecure Design | No separate issues (see A04) | C:0 H:0 M:0 L:0 |
| A07 Authentication Failures | Issues | C:0 H:0 M:1 L:0 |
| A08 Software or Data Integrity Failures | Issues | C:0 H:0 M:1 L:0 |
| A09 Security Logging and Alerting Failures | Issues | C:0 H:1 M:0 L:0 |
| A10 Mishandling of Exceptional Conditions | Issues | C:0 H:0 M:0 L:1 |

The most serious problems all concern cardholder data: full card numbers, tax ids and names are printed to stdout for every record, and card numbers are stored unencrypted in Solr next to names. The classpath also carried many unused, CVE-laden libraries (jackson-databind 2.9.x via Dropwizard 1.3.14, guava 19, commons-io 2.6, bouncycastle 1.64, jose4j 0.7.0) while Jackson global default typing was enabled. Top 3 fixes: (1) stop logging PAN/PII, (2) drop default typing and the vulnerable/unused dependencies, (3) decide on tokenization/encryption of PAN at rest and authenticated TLS to Solr.

## Remediation
| Finding | Severity | Status | Notes |
|---|---|---|---|
| A09-1 PAN, TIN and names written to logs | High | Fixed | `toString()` of all four record/bean classes now masks; new `util.Mask` |
| A03-1 Vulnerable/unused dependencies | High | Fixed | Unused deps removed, rest bumped within same major lines |
| A04-1 PAN stored in cleartext in Solr, joined with names | High | Deferred (needs decision) | Changes data model/trust model |
| A07-1 Solr/DynamoDB accessed over plain HTTP without auth | Medium | Not fixed (recommendation) | Deployment-dependent; needs credentials design |
| A08-1 Jackson global default typing | Medium | Fixed | `enableDefaultTyping()` removed |
| A10-1 Fixed-width parser crashes on malformed lines; errors swallowed | Low | Partially fixed | Parsing is bounds-safe; swallowed exceptions remain |
| A02-1 Hard-coded endpoints and home-directory paths | Info | Not fixed (recommendation) | |

**Verification:** Baseline: `mvn package` (Maven 3.9.9 from the wrapper cache, JDK 26 with `-Dmaven.compiler.fork=true` because the in-process compiler plugin 3.8.0 rejects `--release 11` on JDK 26) built successfully; the project had no tests. After fixes: `mvn clean package -Dmaven.compiler.fork=true` → BUILD SUCCESS, 7 new tests, 0 failures (`src/test/java/util/MaskTest.java`, `src/test/java/util/JSONUtilTest.java`). Negative check: temporarily re-enabling `enableDefaultTyping()` makes `JSONUtilTest.polymorphicTypeIdsAreNotHonoured` fail, so the test guards the fix. `mvn dependency:tree` confirms dropwizard, guava, bouncycastle, jose4j and jackson-databind 2.9/2.6 are gone (jackson-databind 2.18.2 only). No live run against Solr/DynamoDB was possible (neither service is available in the audit environment).
**Breaking changes for operators:** `toString()` of `GVAPDataRecord`/`CHFDataRecord` no longer returns the raw fixed-width record — use `getRecord()` (the file writers already do). The full `aws-java-sdk` bundle is replaced by `aws-java-sdk-dynamodb`; Dropwizard is no longer on the classpath (no code used it); logback is now declared directly.
**Follow-ups:** Any existing console/log output from past runs contains full PANs and names and should be purged. Decide on A04-1 (tokenize or encrypt PAN). Configure Solr authentication + TLS. Transitive residue from solrj 8.11.4 (zookeeper 3.6.2, netty 4.1.99) is the latest available on the 8.x line; moving to SolrJ 9 is a major upgrade left for later.

## Findings

### A01 — Broken Access Control
N/A: no HTTP routes, RPC handlers or authorization logic. File paths are constants (`processor/CHFFileReaderSpout.java:13`, `processor/GVAPFileReaderSpout.java:11`), no SSRF-capable URLs taken from input.

### A02 — Security Misconfiguration

#### [Info] Hard-coded service endpoints and developer paths (Confirmed)
**Status:** Not fixed (recommendation).
- **Location:** `persistence/DynamoDBManager.java:35`, `processor/CHFGVAPMergerBoltSync.java:14`, `processor/CHFGVAPSolrWriterBoltSync.java:17`, `processor/GVAPSolrWriteBoltSync.java:14`, `processor/CHFFileReaderSpout.java:13`
- **Evidence:** `new HttpSolrClient.Builder("http://localhost:8983/solr/" + collection)`, `"/home/manoj/data//chfgvap/chf"`
- **Scenario:** No direct exploit; prevents configuring TLS endpoints/credentials per environment, encouraging plaintext defaults to be shipped.
- **Fix:** Read endpoints and paths from env vars/config with secure defaults. XML parsing is only SolrJ's `XMLResponseParser` (uses a hardened StAX factory); no XXE found.

### A03 — Software Supply Chain Failures

#### [High] Unused and outdated dependencies with known CVEs on the runtime classpath (Confirmed)
**Status:** Fixed — removed `dropwizard-core` 1.3.14, `guava` 19.0, `commons-io` 2.6, `bcprov/bcpkix-jdk15on` 1.64, `jose4j` 0.7.0, `jaxb-*`, `activation`, `jackson-dataformat-cbor`; replaced `aws-java-sdk` 1.11.327 with `aws-java-sdk-dynamodb` 1.12.797; bumped `solr-solrj` 8.6.0→8.11.4, Jackson →2.18.2 (declared explicitly), `junit` 4.12→4.13.2; added `logback-classic` 1.2.13. Verified with build, tests and `dependency:tree`.
- **Location:** `pom.xml` (dependencies block)
- **Evidence:** none of these packages are imported anywhere in `src/` (only SolrJ, Jackson and the DynamoDB SDK are).
- **Scenario:** jackson-databind 2.9.x (from Dropwizard) and 2.6.x (from aws-java-sdk 1.11) have dozens of polymorphic-deserialization gadget CVEs (e.g. CVE-2019-12384, CVE-2020-36518); combined with A08-1 this was a working RCE precondition. guava 19 (CVE-2018-10237, CVE-2023-2976), commons-io 2.6 (CVE-2021-29425), bouncycastle 1.64 (CVE-2020-15522, CVE-2023-33201), jose4j 0.7.0 (CVE-2023-31582, CVE-2023-51775), junit 4.12 (CVE-2020-15250).
- **Fix:** as applied. No OWASP dependency-check / OSV scanner was available locally; versions were reviewed manually.

### A04 — Cryptographic Failures

#### [High] Card numbers stored in cleartext in Solr and joined with names (Confirmed)
**Status:** Deferred (needs decision) — changes the stored data model and how downstream consumers read the `ccv` field.
- **Location:** `processor/GVAPSolrWriteBoltSync.java:26`, `processor/CHFGVAPMergerBoltSync.java:30-36`, `processor/CHFGVAPSolrWriterBoltSync.java:31`
- **Evidence:**
  ```java
  cp.setCcv(record.getPAN());   // full PAN into Solr core "ccvpin"
  bean.setCcv(ccvPin.getCcv()); bean.setFName(...); bean.setLName(...);  // PAN + name into "pinccvpii"
  ```
- **Scenario:** Anyone who can query Solr (which by default has no authentication, see A07-1) can read complete card numbers together with cardholder names and EINs — a PCI DSS 3.4 violation and a high-value breach target.
- **Fix options:** (a) store a token/HMAC of the PAN (keyed, from a secret store) plus last-4 for display and keep real PANs in a vault; (b) encrypt the field with AES-GCM using a managed key before indexing (field becomes non-searchable); (c) if the merge only needs a join, join on EIN and never store PAN at all. Choose based on what consumers of `pinccvpii` need.

### A05 — Injection
No issues found. The only Solr query built from data is `"ein:" + chfRecord.getEin()` (`processor/CHFGVAPMergerBoltSync.java:26`), where `getEin()` returns an `int`, so no query-syntax injection is possible. DynamoDB access uses `DynamoDBMapper` (no expression strings). No process execution, scripting or HTML output.

### A06 — Insecure Design
Covered by A04-1 (linking PAN with names in a searchable index is the core design risk). No other design-level issues beyond the prototype's lack of configuration.

### A07 — Authentication Failures

#### [Medium] Solr and DynamoDB accessed over plain HTTP with no authentication (Plausible)
**Status:** Not fixed (recommendation) — requires choosing a credential source and enabling auth on the servers.
- **Location:** `util/SolrClient.java:23-24`, `persistence/DynamoDBManager.java:34-36`
- **Evidence:** `new HttpSolrClient.Builder(urlString).build();` (no credentials, `http://`)
- **Scenario:** If Solr is reachable beyond localhost (or another local user/process shares the host), it can be read and written without credentials, exposing PANs and allowing poisoned documents to be fed back into the merge. Confirmed only if the deployed Solr is unauthenticated.
- **Fix:** Enable Solr BasicAuth/PKI + TLS and pass credentials via `QueryRequest.setBasicAuthCredentials` / an authenticated `HttpClient`; read endpoint and credentials from the environment.

### A08 — Software or Data Integrity Failures

#### [Medium] Jackson global default typing on data read back from Solr (Confirmed)
**Status:** Fixed — `mapper.enableDefaultTyping()` removed from `util/JSONUtil.java`; regression test `JSONUtilTest.polymorphicTypeIdsAreNotHonoured` (fails if default typing is re-enabled).
- **Location:** `util/JSONUtil.java:16`, used by `util/SolrClient.java:67,95`
- **Evidence:** `mapper.enableDefaultTyping();`
- **Scenario:** Documents in Solr (writable without auth, A07-1) are deserialized with `JSONUtil.fromJSON`. With default typing, any `Object`/abstract-typed property honours attacker-chosen class names — the classic Jackson gadget RCE, made practical by the old jackson-databind on the classpath. Current beans have only concrete fields, so this was latent rather than directly exploitable.
- **Fix:** as applied; if polymorphism is ever needed use `activateDefaultTyping` with a `BasicPolymorphicTypeValidator` allow-list.

### A09 — Security Logging and Alerting Failures

#### [High] Full card numbers, tax ids and names written to stdout (Confirmed)
**Status:** Fixed — `toString()` of `GVAPDataRecord`, `CHFDataRecord`, `CcvPin`, `PinCCVPii` now uses `util.Mask` (PAN → last 4, TIN → `****`, names → initial). `getRecord()` (file format) is unchanged. Verified by `MaskTest`.
- **Location:** `event/GVAPDataRecord.java:145-148`, `event/CHFDataRecord.java:149-152`, `event/CcvPin.java:44-50`, `event/PinCCVPii.java:60-67`; sinks `graph/TransactionGraph.java:24`, `processor/SysOutBoltSync.java:10`
- **Evidence:**
  ```java
  public String toString() { return getRecord(); }   // raw record: EIN + TIN + PAN
  System.out.println("Added event to graph " + event);
  ```
- **Scenario:** Every processed record is printed in full to the console/log collector, so anyone with log access obtains complete PANs, TINs and names (PCI DSS 3.3/10 violation).
- **Fix:** as applied. Previously captured logs should be purged.

### A10 — Mishandling of Exceptional Conditions

#### [Low] Fixed-width parser fails on malformed lines; I/O and Solr errors swallowed (Confirmed)
**Status:** Partially fixed — `GVAPDataRecord.create(String)` / `CHFDataRecord.create(String)` now copy input into the fixed-size buffer, so short lines no longer throw `ArrayIndexOutOfBoundsException` in getters/`toString` (tested). Swallowed exceptions (`printStackTrace` + empty results in `util/SolrClient.java:71-103`, the file readers) remain: a Solr outage silently produces an empty merge.
- **Location:** `event/GVAPDataRecord.java:29-37`, `event/CHFDataRecord.java:29-37`, `util/SolrClient.java:71-103`
- **Scenario:** One truncated input line crashed the topology thread; Solr failures are indistinguishable from "no match", causing silent data loss.
- **Fix:** propagate or count failures and fail the batch; validate numeric fields before `Integer.valueOf`.

## Out of scope / not checked
- No dependency scanner (OWASP dependency-check, OSV-Scanner, Snyk) was installed; dependency CVEs were assessed manually from versions.
- Solr and DynamoDB server configuration (auth, TLS, network exposure) could not be inspected; no live run was performed.
- Git history contains no key/keystore/.env files (`git log --all -- '*.pem' '*.key' '*.jks' '.env'` returned nothing); no hard-coded credentials found in source.
- The `.idea/` project files were not reviewed for security relevance beyond a skim.
