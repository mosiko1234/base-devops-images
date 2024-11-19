# Start with a lightweight Linux base image
FROM ubuntu:20.04

# Set environment variables
ENV PATH="/root/.local/bin:$PATH"
ENV DEBIAN_FRONTEND=noninteractive
# Define shell with pipefail
SHELL ["/bin/bash", "-o", "pipefail", "-c"]
# Create a non-root user
RUN useradd -m devops
USER devops

# Update system packages and install essential tools
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential=12.4 \
    curl=7.68 \
    jq=1.5-1 \
    bash=5.0 \
    unzip=6.0-25 \
    wget=1.20 \
    vim=2:8.1 \
    ca-certificates=20211016 \
    openssh-client=1:8.2 \
    software-properties-common=0.99.9 \
    apt-transport-https=2.0 \
    gnupg=2.2 \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# Install AWS CLI
RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" \
    && unzip awscliv2.zip \
    && ./aws/install \
    && rm -rf aws awscliv2.zip

# Install yq (YAML processor for DevOps pipelines)
RUN curl -sL "https://github.com/mikefarah/yq/releases/latest/download/yq_linux_amd64" -o /usr/bin/yq \
    && chmod +x /usr/bin/yq

# Install Helm (Kubernetes package manager)
RUN curl https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash

# Install OpenShift CLI (oc)
RUN curl -L "https://mirror.openshift.com/pub/openshift-v4/clients/ocp/latest/openshift-client-linux.tar.gz" -o oc.tar.gz \
    && tar -xvzf oc.tar.gz -C /usr/local/bin/ \
    && chmod +x /usr/local/bin/oc \
    && rm oc.tar.gz

# Install ArgoCD CLI
RUN curl -sSL -o /usr/local/bin/argocd https://github.com/argoproj/argo-cd/releases/latest/download/argocd-linux-amd64 \
    && chmod +x /usr/local/bin/argocd

# Install GitLab Runner CLI
RUN curl -L "https://gitlab-runner-downloads.s3.amazonaws.com/v16.5.0/binaries/gitlab-runner-linux-amd64" -o /usr/local/bin/gitlab-runner \
    && chmod +x /usr/local/bin/gitlab-runner

# Set working directory
WORKDIR /workspace

# Default entrypoint
ENTRYPOINT ["/bin/bash"]
