# Start with Ubuntu 24.04 LTS
FROM ubuntu:24.04

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive \
    PATH="/usr/local/bin:$PATH"

# Define shell with pipefail
SHELL ["/bin/bash", "-o", "pipefail", "-c"]

# Create non-root user
RUN useradd -m -s /bin/bash devops \
    && mkdir -p /workspace \
    && chown devops:devops /workspace

# Update system packages and install essential tools
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        build-essential \
        libxml2-utils \
        libc6 \
        libc6-dev \
        libc-bin \
        curl \
        git \
        jq \
        bash \
        unzip \
        wget \
        ca-certificates \
        openssh-client \
        gnupg \
        software-properties-common \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* /var/cache/apt/* /tmp/* /var/tmp/*

# Install tools — architecture-aware for multi-platform builds
RUN ARCH=$(uname -m) \
    && case "$ARCH" in \
        x86_64)  AWS_ARCH="x86_64"; BIN_ARCH="amd64" ;; \
        aarch64) AWS_ARCH="aarch64"; BIN_ARCH="arm64" ;; \
        *) echo "Unsupported architecture: $ARCH" && exit 1 ;; \
    esac \
    && cd /tmp \
    # AWS CLI
    && curl -sSL "https://awscli.amazonaws.com/awscli-exe-linux-${AWS_ARCH}.zip" -o awscliv2.zip \
    && unzip -q awscliv2.zip \
    && ./aws/install \
    && rm -rf aws awscliv2.zip \
    # yq
    && curl -sSL "https://github.com/mikefarah/yq/releases/latest/download/yq_linux_${BIN_ARCH}" -o /usr/bin/yq \
    && chmod +x /usr/bin/yq \
    # Helm (auto-detects architecture)
    && curl https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash \
    # OpenShift CLI (stable channel, arch-aware)
    && curl -sSL "https://mirror.openshift.com/pub/openshift-v4/${ARCH}/clients/ocp/stable/openshift-client-linux.tar.gz" -o oc.tar.gz \
    && tar -xzf oc.tar.gz -C /usr/local/bin/ \
    && chmod +x /usr/local/bin/oc \
    && rm -f oc.tar.gz /usr/local/bin/README.md \
    # ArgoCD CLI
    && curl -sSL -o /usr/local/bin/argocd "https://github.com/argoproj/argo-cd/releases/latest/download/argocd-linux-${BIN_ARCH}" \
    && chmod +x /usr/local/bin/argocd \
    # GitLab Runner
    && curl -sSL "https://gitlab-runner-downloads.s3.amazonaws.com/v18.9.0/binaries/gitlab-runner-linux-${BIN_ARCH}" -o /usr/local/bin/gitlab-runner \
    && chmod +x /usr/local/bin/gitlab-runner \
    # Clean up
    && rm -rf /tmp/*

# Set proper permissions for all binaries
RUN chmod 755 /usr/local/bin/* \
    && chmod 755 /usr/bin/yq

# Set working directory and permissions
WORKDIR /workspace
RUN chown devops:devops /workspace

# Switch to non-root user
USER devops

# Default entrypoint
ENTRYPOINT ["/bin/bash"]
