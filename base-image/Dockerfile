# Start with a lightweight Ubuntu image
FROM --platform=linux/amd64 ubuntu:20.04 AS base

# Environment variables
ENV PATH="/root/.local/bin:$PATH" \
    DEBIAN_FRONTEND=noninteractive

# Define shell with pipefail
SHELL ["/bin/bash", "-o", "pipefail", "-c"]

# Install essential tools and clean up after installation
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    libxml2-utils \
    curl \
    git \
    jq \
    bash \
    unzip \
    wget \
    ca-certificates \
    openssh-client \
    gnupg \
    apt-transport-https && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Install AWS CLI
RUN curl -s "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o awscliv2.zip && \
    unzip awscliv2.zip && ./aws/install && \
    rm -rf aws awscliv2.zip

# Install yq (YAML processor)
RUN wget -qO /usr/bin/yq https://github.com/mikefarah/yq/releases/latest/download/yq_linux_amd64 && \
    chmod +x /usr/bin/yq

# Install Helm (Kubernetes package manager)
RUN curl -s https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash

# Install OpenShift CLI (oc)
RUN wget -qO oc.tar.gz "https://mirror.openshift.com/pub/openshift-v4/clients/ocp/4.8.0/openshift-client-linux.tar.gz" && \
    tar -xzf oc.tar.gz -C /usr/local/bin && chmod +x /usr/local/bin/oc && \
    rm oc.tar.gz

# Install ArgoCD CLI
RUN wget -qO /usr/local/bin/argocd https://github.com/argoproj/argo-cd/releases/latest/download/argocd-linux-amd64 && \
    chmod +x /usr/local/bin/argocd

# Install GitLab Runner CLI
RUN wget -qO /usr/local/bin/gitlab-runner "https://gitlab-runner-downloads.s3.amazonaws.com/v16.5.0/binaries/gitlab-runner-linux-amd64" && \
    chmod +x /usr/local/bin/gitlab-runner

# Set working directory
WORKDIR /workspace

# Default entrypoint
ENTRYPOINT ["/bin/bash"]
