

# SiX IoT Baidu XiaoDu (DuerOS) Sample

## Companion projects

To build a fully integrated smart-home experience from firmware to application layer, check out our companion open-source repositories:

*   **Firmware Development:** [six-iot-sdk-esp32](https://github.com/Simple-intelligent-X/six-iot-sdk-esp32) — The reference Chipset SDK for building MQTT-compliant firmware applications directly on Espressif targets.

*   **Mobile Application Integration:** [six-iot-sdk-android](https://github.com/Simple-intelligent-X/six-iot-sdk-android) — The native mobile SDK providing complete blueprints for OIDC user authentication, Wi-Fi network provisioning, and device control loops.

## Project Overview

A **Spring Boot** reference implementation demonstrating a Cloud-to-Cloud (C2C) integration between the **SiX IoT Platform** and **Baidu XiaoDu (DuerOS)**. This repository provides a foundational smart-home skill webhook that supports device discovery and basic control operations (`turnOn`, `turnOff`).

> [!TIP]
> We highly recommend reviewing the [XiaoDu Integration Architecture Guide](https://doc.iot.shuhenglianchang.com/voice/xiaodu) to fully understand the Cloud-to-Cloud authentication flow before testing. 

> Please also review the [Inbound Integration Guide](https://doc.iot.shuhenglianchang.com/integration/inbound) to learn how to securely interact with your IoT devices using an API key.

---

## Quick Links

Explore the core components of the integration:
*   **Build File:** [`build.gradle`](build.gradle)
*   **Application Entry:** [`src/main/java/com/six/iot/sample/VoiceApplication.java`](src/main/java/com/six/iot/sample/VoiceApplication.java)
*   **Skill Handler (Webhook):** [`src/main/java/com/six/iot/sample/web/controller/SkillController.java`](src/main/java/com/six/iot/sample/web/controller/SkillController.java)
*   **Environment Settings:** [`src/main/resources/application.yml`](src/main/resources/application.yml)

---

## Prerequisites

Ensure your development environment meets the following requirements:

*   **Java Runtime:** Java 17 or newer.
*   **Build Tool:** Gradle *(The project includes the standard Gradle wrapper `gradlew`)*.
*   **Local SDK:** Ensure `libs/sdk-1.0-SNAPSHOT.jar` (the local SiX IoT SDK) is present in the `libs/` directory.
*   **Network Access:** Your local machine must be able to reach the SiX IAM and IoT API endpoints configured in your properties file.

---

## Build & Run

From the project root directory, you can compile and boot the application.

### Build the Application
**Linux / macOS:**
```bash
./gradlew clean build
```
**Windows (CMD / PowerShell):**
```powershell
.\gradlew.bat clean build
```

### Run the Local Server
**Linux / macOS:**
```bash
./gradlew bootRun
```
**Windows (CMD / PowerShell):**
```powershell
.\gradlew.bat bootRun
```
*By default, the embedded server will start and listen on port `6002`.*

---

## Configuration

Before running the application, map your specific tenant values inside `src/main/resources/application.yml`:

*   `iam.token.endpoint` — The OAuth2 token issuance endpoint for your SiX IAM application.
*   `iot.integration.inboundMsg.keyId` — Your integration RSA Key ID.
*   `iot.integration.inboundMsg.key` — Your private RSA signing key.
*   `iot.integration.productId` — Your unique SiX IoT product identifier.
*   `skill.device.userDevicesEndpoint` — The IoT API endpoint used to query a user's bound devices.

> [!WARNING]
> **Security Notice:** The repository contains a placeholder, dummy RSA private key within `application.yml` for structural reference. **Do not use this key.** You must replace it with your real cryptographic key and secure it properly before exposing this webhook to the public internet.

---

## Architecture: How It Works

1.  **Incoming Webhook:** XiaoDu (DuerOS) sends a skill request JSON payload to `POST /skill/handler`.
2.  **Device Discovery:** For discovery intents, the controller queries the IoT `userDevicesEndpoint` using the user's OAuth access token and reformats the response into a compliant DuerOS appliance list.
3.  **Device Control:** For control intents (`turnOn`/`turnOff`), the controller generates a signed JWT using the inbound private key, fetches a backend integration access token from SiX IAM, and publishes an MQTT control message directly to the device's topic stream (`productId/deviceGuid/light`).

---

## Endpoints & API Testing

The single entry point for all DuerOS requests is:
**`POST /skill/handler`**

Below are example `curl` commands simulating inbound requests from the Baidu XiaoDu cloud. *(Remember to replace `<user-access-token>` and `<deviceGuid>` with live values).*

### 1. Discover Appliances
```bash
curl -X POST http://localhost:6002/skill/handler \
  -H "Content-Type: application/json" \
  -d '{
    "header": {
      "namespace": "DuerOS.ConnectedHome.Discovery",
      "name": "DiscoverAppliancesRequest",
      "payloadVersion": "1"
    },
    "payloadVersion": "1",
    "accessToken": "<user-access-token>"
  }'
```

### 2. Turn On Appliance
```bash
curl -X POST http://localhost:6002/skill/handler \
  -H "Content-Type: application/json" \
  -d '{
    "header": {
      "namespace": "DuerOS.ConnectedHome.Control",
      "name": "TurnOnRequest",
      "payloadVersion": "1"
    },
    "payloadVersion": "1",
    "payload": {
      "appliance": {
        "applianceId": "<deviceGuid>"
      }
    }
  }'
```

### 3. Turn Off Appliance
```bash
curl -X POST http://localhost:6002/skill/handler \
  -H "Content-Type: application/json" \
  -d '{
    "header": {
      "namespace": "DuerOS.ConnectedHome.Control",
      "name": "TurnOffRequest",
      "payloadVersion": "1"
    },
    "payloadVersion": "1",
    "payload": {
      "appliance": {
        "applianceId": "<deviceGuid>"
      }
    }
  }'
```

---

## Testing Tips

*   **Public Routing:** Baidu DuerOS requires a public-facing HTTPS URL to dispatch webhook events. Use tools like [ngrok](https://ngrok.com/) or Cloudflare Tunnels to securely expose your local port `6002` to the internet during development.
*   **Logging:** To trace inbound payloads and token exchanges, enable debug-level logging for the SiX SDK by adding the appropriate log level for `com.six.iot` within your `application.yml`.

---

## License

This repository is licensed under the **Apache-2.0 License**. See the [LICENSE](LICENSE) file for full terms and conditions.

## Contact & Support

For architectural discussions, private identity federation setup, or custom enterprise integration feedback:

*   **Engineering Support:** stephen.yu@six-inno.cn
*   **Enterprise Service:** service@six-inno.cn