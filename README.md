
# Docker Multi-Language Images Pipeline

This repository contains a robust pipeline for building, testing, and pushing Docker images tailored for different programming environments. The project is designed to streamline the process of maintaining base images and language-specific images for Python and Java while ensuring high-quality builds and optimal performance.

---

## Features

1. **Base Image:**
   - A common foundational image with essential tools for DevOps operations.
   - Includes AWS CLI, Helm, ArgoCD CLI, GitLab Runner, and other utilities.

2. **Python Images:**
   - Dynamically builds Python images for multiple versions (e.g., 3.11, 3.12).
   - Leverages `deadsnakes` PPA for the latest Python versions.
   - Includes `pip`, `venv`, and all necessary dependencies.

3. **Java Images:**
   - Builds Java images for multiple versions (e.g., 11, 17).
   - Uses OpenJDK for compatibility and consistency.
   - Configurable for specific Java runtime needs.

4. **Linting:**
   - Uses `hadolint` to ensure Dockerfiles follow best practices.
   - Can be customized or extended for stricter compliance.

5. **Multi-Platform Builds:**
   - Supports `linux/amd64` and `linux/arm64` architectures using Docker Buildx.

6. **CI/CD Pipeline:**
   - Automatically triggered on `main` branch pushes.
   - Efficient caching with `--cache-from` and `--cache-to` for faster builds.
   - Matrix strategy to handle multiple versions of Python and Java concurrently.

---

## Repository Structure

```plaintext
.
├── base-image/
│   ├── Dockerfile          # Defines the base image with essential tools.
├── python-layer/
│   ├── Dockerfile          # Builds Python-specific images.
├── java-layer/
│   ├── Dockerfile          # Builds Java-specific images.
├── .github/workflows/
│   ├── build-pipeline.yml  # The CI/CD pipeline for building and pushing images.
└── README.md               # Project documentation.
```

---

## Prerequisites

- **Docker:** Ensure Docker and Docker Buildx are installed and configured.
- **GitHub Actions Secrets:**
  - `DOCKERHUB_USERNAME`: Your DockerHub username.
  - `DOCKERHUB_PASSWORD`: Your DockerHub password.
  - `DOCKERHUB_REPO`: The target DockerHub repository.

---

## Pipeline Workflow

### Steps Overview

1. **Build and Push Base Image:**
   - Builds a reusable base image with essential tools.
   - Pushes the image to DockerHub with `latest` and versioned tags.

2. **Build and Push Python Images:**
   - Iterates over Python versions using a matrix strategy.
   - Builds and pushes versioned Python images (e.g., `python-3.11`).

3. **Build and Push Java Images:**
   - Iterates over Java versions using a matrix strategy.
   - Builds and pushes versioned Java images (e.g., `java-11`).

4. **Linting (Optional):**
   - Ensures Dockerfiles comply with best practices using `hadolint`.

---

## How to Use

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/yourusername/docker-multi-language-pipeline.git
   cd docker-multi-language-pipeline
   ```

2. **Configure GitHub Actions:**
   - Add DockerHub credentials (`DOCKERHUB_USERNAME`, `DOCKERHUB_PASSWORD`, `DOCKERHUB_REPO`) to GitHub Secrets.

3. **Run the Pipeline:**
   - Push changes to the `main` branch to trigger the CI/CD pipeline.

4. **Verify Images:**
   - Verify the pushed images on DockerHub:
     - `base-image`
     - `python-3.11`, `python-3.12`
     - `java-11`, `java-17`

---

## Example Commands

### Test a Built Image Locally
```bash
docker pull yourdockerhub/base-image:latest
docker run --rm -it yourdockerhub/base-image:latest bash
```

### Add a New Language Version
1. Modify the matrix strategy in `.github/workflows/build-pipeline.yml`:
   ```yaml
   matrix:
     python_version: [3.11, 3.12, 3.13]
     java_version: [11, 17, 19]
   ```

2. Update the corresponding Dockerfiles if necessary.

---

## Contribution Guidelines

1. Fork the repository and create a new branch.
2. Make changes and test locally.
3. Submit a pull request with a clear description of changes.

---

## Known Issues & Troubleshooting

1. **Linting Errors:**
   - Use `hadolint` locally to debug and fix linting issues:
     ```bash
     docker run --rm -i hadolint/hadolint < ./base-image/Dockerfile
     ```

2. **Build Cache Errors:**
   - Clear Docker cache if builds are inconsistent:
     ```bash
     docker builder prune --all
     ```

3. **QEMU Emulator Issues (ARM64):**
   - Ensure QEMU is properly configured for multi-architecture builds:
     ```bash
     docker run --rm --privileged multiarch/qemu-user-static --reset -p yes
     ```

---

## Future Enhancements

- Add Node.js images to support JavaScript applications.
- Integrate security scanning tools like `trivy` for Docker images.
- Automate semantic versioning for image tags.

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---
