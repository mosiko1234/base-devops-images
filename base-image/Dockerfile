FROM --platform=${BUILDPLATFORM:-linux/amd64} ubuntu:20.04 AS builder

LABEL maintainer="your-email@example.com" \
      version="1.0.0" \
      description="Development tools container with Helm, ArgoCD, and GitLab Runner" \
      org.opencontainers.image.source="https://github.com/yourusername/devops-tools"

ENV PATH="/root/.local/bin:$PATH" \
    DEBIAN_FRONTEND=noninteractive \
    LANG=C.UTF-8

RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    git \
    jq \
    bash \
    unzip \
    wget \
    ca-certificates \
    gnupg \
    openssh-client \
    apt-transport-https && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

ARG YQ_VERSION=v4.40.5
RUN wget -qO /usr/bin/yq "https://github.com/mikefarah/yq/releases/download/${YQ_VERSION}/yq_linux_amd64" && \
    chmod +x /usr/bin/yq

ARG HELM_VERSION=v3.13.3
RUN curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 && \
    chmod +x get_helm.sh && \
    DESIRED_VERSION=${HELM_VERSION} ./get_helm.sh && \
    rm get_helm.sh

ARG TARGETARCH
ARG OC_VERSION=4.13.0
RUN case "${TARGETARCH}" in \
        "amd64") ARCH="x86_64" ;; \
        "arm64") ARCH="aarch64" ;; \
        "ppc64le") ARCH="ppc64le" ;; \
        "s390x") ARCH="s390x" ;; \
        *) echo "Unsupported architecture: ${TARGETARCH}" && exit 1 ;; \
    esac && \
    curl -L --retry 5 --retry-delay 2 -o oc.tar.gz "https://mirror.openshift.com/pub/openshift-v4/clients/ocp/stable-${OC_VERSION}/openshift-client-linux-${ARCH}.tar.gz" && \
    tar -xzf oc.tar.gz -C /usr/local/bin && \
    chmod +x /usr/local/bin/oc && \
    rm -f oc.tar.gz kubectl && \
    oc version --client || true

ARG ARGO_VERSION=v2.9.3
RUN curl -sSL -o /usr/local/bin/argocd "https://github.com/argoproj/argo-cd/releases/download/${ARGO_VERSION}/argocd-linux-amd64" && \
    chmod +x /usr/local/bin/argocd

ARG GITLAB_RUNNER_VERSION=v16.5.0
RUN curl -sSL -o /usr/local/bin/gitlab-runner "https://gitlab-runner-downloads.s3.amazonaws.com/${GITLAB_RUNNER_VERSION}/binaries/gitlab-runner-linux-amd64" && \
    chmod +x /usr/local/bin/gitlab-runner

RUN yq --version && \
    helm version --short && \
    if [ -f "/usr/local/bin/oc" ]; then oc version --client; else echo "oc not available"; fi && \
    argocd version --client && \
    gitlab-runner --version

FROM ubuntu:20.04 AS final

ENV PATH="/root/.local/bin:$PATH" \
    DEBIAN_FRONTEND=noninteractive \
    LANG=C.UTF-8

RUN apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates \
    curl \
    bash \
    gnupg \
    openssh-client && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

COPY --from=builder /usr/bin/yq /usr/bin/yq
COPY --from=builder /usr/local/bin/helm /usr/local/bin/helm
COPY --from=builder /usr/local/bin/oc* /usr/local/bin/
COPY --from=builder /usr/local/bin/argocd /usr/local/bin/argocd
COPY --from=builder /usr/local/bin/gitlab-runner /usr/local/bin/gitlab-runner

RUN yq --version && \
    helm version --short && \
    if [ -f "/usr/local/bin/oc" ]; then oc version --client; else echo "oc not available"; fi && \
    argocd version --client && \
    gitlab-runner --version

WORKDIR /workspace
ENTRYPOINT ["/bin/bash"]