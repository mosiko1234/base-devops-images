# Start with a lightweight Linux base image
FROM debian:bookworm-slim

# Set environment variables
ENV PATH="/root/.local/bin:$PATH"

USER root
# Update system packages and install essential tools
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    curl \
    git \
    jq \
    bash \
    unzip \
    wget \
    ca-certificates \
    openssh-client \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Install AWS CLI
RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" \
    && unzip awscliv2.zip \
    && ./aws/install \
    && rm -rf aws awscliv2.zip

# Install yq (YAML processor for DevOps pipelines)
RUN wget -q "https://github.com/mikefarah/yq/releases/latest/download/yq_linux_amd64" -O /usr/bin/yq \
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


# Add a non-root user for security
RUN useradd -ms /bin/bash devops
USER devops

# Set working directory
WORKDIR /workspace

# Entrypoint setup
ENTRYPOINT ["/bin/bash"]
