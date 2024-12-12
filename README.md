
# Base DevOps Images

**Maintainer**: [Moshe Eliya](https://github.com/mosiko1234)  
**Repository**: [mosiko1234/base-devops-images](https://github.com/mosiko1234/base-devops-images)

## Overview

This repository provides an automated pipeline for building, testing, and deploying Docker images for various programming languages. The pipeline supports dynamic versioning and integrates security scans and notifications for a seamless DevOps experience.

## Features

- **Base Image**: A foundational image with essential tools for DevOps (e.g., AWS CLI, yq, Helm, OpenShift CLI, ArgoCD CLI).
- **Dynamic Language Layers**: Builds language-specific Docker images for Java and Python with configurable versions.
- **Automated CI/CD**: Leveraging GitHub Actions to handle the entire process from build to deployment.
- **Security Scanning**: Trivy scans to ensure image safety.
- **Slack Notifications**: Sends detailed build summaries.

---

## Project Structure

```plaintext
.
├── LICENSE                   # License file for the repository
├── README.md                 # Project documentation
├── base-image
│   └── Dockerfile            # Base image definition
├── config.yml                # Configuration file for language versions
├── java-layer
│   └── Dockerfile.java       # Dockerfile for Java images
├── python-layer
│   └── Dockerfile.python     # Dockerfile for Python images
```

---

## Getting Started

### Prerequisites

1. **DockerHub Account**:
   - Add the following secrets to your GitHub repository:
     - `DOCKERHUB_USERNAME`
     - `DOCKERHUB_PASSWORD`

2. **Slack Webhook**:
   - Add the `SLACK_WEBHOOK_URL` secret to enable Slack notifications.

### Configuration

Edit the `config.yml` file to define the languages and versions to build:

```yaml
languages:
  python:
    versions:
      - 3.11
      - 3.12
  java:
    versions:
      - 11
      - 17
```

### Pipeline Workflow

The GitHub Actions pipeline is triggered by pushes to the `config-dev` branch. The workflow includes:

1. **Base Image Build**:
   - Creates a shared base image with pre-installed DevOps tools.
2. **Dynamic Matrix Generation**:
   - Parses `config.yml` to generate build tasks dynamically.
3. **Language-Specific Builds**:
   - Builds Docker images for each language/version combination.
4. **Security Scanning**:
   - Trivy scans all images for vulnerabilities.
5. **Notifications**:
   - Sends Slack messages with build and test summaries.

---

## Docker Images

### Base Image

The base image is defined in `base-image/Dockerfile` and includes:
- Lightweight Ubuntu 20.04.
- DevOps tools like AWS CLI, Helm, OpenShift CLI, and more.

### Java Layer

- Dockerfile: `java-layer/Dockerfile.java`
- Dynamically installs OpenJDK versions.
- Example versions: 11, 17.

### Python Layer

- Dockerfile: `python-layer/Dockerfile.python`
- Dynamically installs Python versions.
- Example versions: 3.11, 3.12.

### Node.js Images
The Node.js images are built with support for the following versions:
- Node.js 14
- Node.js 16
- Node.js 18


---

## Outputs

### Artifacts

- **Trivy Scan Reports**:
  - Uploaded to GitHub as workflow artifacts.
  - Stored in the `./trivy-scan-report` directory during runtime.

### Slack Notifications

Slack notifications include:
- Repository details.
- Build status.
- List of built images.
- Security scan summary.

Example notification:

```
:information_source: *Build and Test Results*
Repository: mosiko1234/base-devops-images
Branch: config-dev
Build Status: :white_check_mark: Build and Tests Completed Successfully
Images Built:
• python-3.11
• python-3.12
• java-11
• java-17
Total Trivy Reports: 4 reports generated
Workflow Run: View Details
```

---

## How to Use

### Clone the Repository

```bash
git clone https://github.com/mosiko1234/base-devops-images.git
cd base-devops-images
```

### Modify Configuration

Update the `config.yml` file with the desired language versions.

### Trigger the Workflow

Push changes to the `config-dev` branch:

```bash
git add .
git commit -m "Update config.yml"
git push origin config-dev
```

The pipeline will automatically run and build the images.

---

## Contribution Guidelines

1. **Fork the Repository**.
2. Create a new branch for your changes:
   ```bash
   git checkout -b feature-name
   ```
3. Commit your changes:
   ```bash
   git commit -m "Add feature-name"
   ```
4. Push to your forked repository and create a pull request.

---

## License

This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for details.

---

## Maintainer

**Moshe Eliya**  
GitHub: [mosiko1234](https://github.com/mosiko1234)

For any issues or questions, feel free to open an issue in the repository.
