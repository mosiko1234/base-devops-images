# שלב הבנייה - התקנת כל הכלים
FROM ubuntu:20.04 AS builder

ENV PATH="/root/.local/bin:$PATH" \
    DEBIAN_FRONTEND=noninteractive

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

RUN wget -qO /usr/bin/yq https://github.com/mikefarah/yq/releases/latest/download/yq_linux_amd64 && \
    chmod +x /usr/bin/yq

RUN curl -s https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash

RUN wget -qO oc.tar.gz "https://mirror.openshift.com/pub/openshift-v4/clients/ocp/4.8.0/openshift-client-linux.tar.gz" && \
    tar -xzf oc.tar.gz -C /usr/local/bin && chmod +x /usr/local/bin/oc && \
    rm oc.tar.gz

RUN wget -qO /usr/local/bin/argocd https://github.com/argoproj/argo-cd/releases/latest/download/argocd-linux-amd64 && \
    chmod +x /usr/local/bin/argocd

RUN wget -qO /usr/local/bin/gitlab-runner "https://gitlab-runner-downloads.s3.amazonaws.com/v16.5.0/binaries/gitlab-runner-linux-amd64" && \
    chmod +x /usr/local/bin/gitlab-runner

RUN yq --version && \
    helm version && \
    oc version --client && \
    argocd version --client && \
    gitlab-runner --version

FROM ubuntu:20.04 AS final

ENV PATH="/root/.local/bin:$PATH" \
    DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates \
    curl \
    bash \
    gnupg \
    openssh-client && \
    apt-get clean && rm -rf /var/lib/apt/lists/*


COPY --from=builder /usr/bin/yq /usr/bin/yq
COPY --from=builder /usr/local/bin/helm /usr/local/bin/helm
COPY --from=builder /usr/local/bin/oc /usr/local/bin/oc
COPY --from=builder /usr/local/bin/argocd /usr/local/bin/argocd
COPY --from=builder /usr/local/bin/gitlab-runner /usr/local/bin/gitlab-runner

RUN yq --version && \
    helm version && \
    oc version --client && \
    argocd version --client && \
    gitlab-runner --version

WORKDIR /workspace

ENTRYPOINT ["/bin/bash"]
