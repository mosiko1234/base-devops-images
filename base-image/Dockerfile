# Builder stage
FROM ubuntu:20.04 AS builder

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive \
    LANG=C.UTF-8 \
    LC_ALL=C.UTF-8

# Install necessary build tools and dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    libc6 \
    libc6-dev \
    libc-bin \
    curl \
    git \
    bash \
    unzip \
    wget \
    vim \
    ca-certificates \
    gnupg \
    apt-transport-https \
    software-properties-common \
    && add-apt-repository -y ppa:ubuntu-toolchain-r/test \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*
# Install AWS CLI
RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" \
    && unzip awscliv2.zip \
    && ./aws/install \
    && rm -rf aws awscliv2.zip

# Install yq
RUN curl -sL "https://github.com/mikefarah/yq/releases/latest/download/yq_linux_amd64" -o /usr/bin/yq \
    && chmod +x /usr/bin/yq

# Install Helm
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

# Final stage
FROM ubuntu:20.04

# Copy tools and configurations from the builder stage
COPY --from=builder /usr/local/bin /usr/local/bin
COPY --from=builder /usr/bin/yq /usr/bin/yq

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive \
    LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    PATH="/root/.local/bin:$PATH"

# Install runtime dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    git \
    bash \
    vim \
    ca-certificates \
    openssh-client \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /workspace

# Define default entrypoint
ENTRYPOINT ["/bin/bash"]
