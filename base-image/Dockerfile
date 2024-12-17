# שלב הבנייה - התקנת כל הכלים
FROM ubuntu:20.04 AS builder

# הגדרות סביבה
ENV PATH="/root/.local/bin:$PATH" \
    DEBIAN_FRONTEND=noninteractive

# התקנת כלים נדרשים לבנייה
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

# התקנת yq (YAML processor)
RUN wget -qO /usr/bin/yq https://github.com/mikefarah/yq/releases/latest/download/yq_linux_amd64 && \
    chmod +x /usr/bin/yq

# התקנת Helm
RUN curl -s https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash

# התקנת OpenShift CLI (oc)
RUN wget -qO oc.tar.gz "https://mirror.openshift.com/pub/openshift-v4/clients/ocp/4.8.0/openshift-client-linux.tar.gz" && \
    tar -xzf oc.tar.gz -C /usr/local/bin && chmod +x /usr/local/bin/oc && \
    rm oc.tar.gz

# התקנת ArgoCD CLI
RUN wget -qO /usr/local/bin/argocd https://github.com/argoproj/argo-cd/releases/latest/download/argocd-linux-amd64 && \
    chmod +x /usr/local/bin/argocd

# התקנת GitLab Runner CLI
RUN wget -qO /usr/local/bin/gitlab-runner "https://gitlab-runner-downloads.s3.amazonaws.com/v16.5.0/binaries/gitlab-runner-linux-amd64" && \
    chmod +x /usr/local/bin/gitlab-runner

# בדיקת הכלים בשלב הבנייה
RUN yq --version && \
    helm version --short && \
    oc version --client && \
    argocd version --client && \
    gitlab-runner --version

# שלב סופי - יצירת אימג' רזה
FROM ubuntu:20.04 AS final

# הגדרות סביבה
ENV PATH="/root/.local/bin:$PATH" \
    DEBIAN_FRONTEND=noninteractive

# התקנת תלות מינימלית בלבד
RUN apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates \
    curl \
    bash \
    gnupg \
    openssh-client && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# העתקת הכלים מהשלב הקודם
COPY --from=builder /usr/bin/yq /usr/bin/yq
COPY --from=builder /usr/local/bin/helm /usr/local/bin/helm
COPY --from=builder /usr/local/bin/oc /usr/local/bin/oc
COPY --from=builder /usr/local/bin/argocd /usr/local/bin/argocd
COPY --from=builder /usr/local/bin/gitlab-runner /usr/local/bin/gitlab-runner

# בדיקת גרסאות בשלב הסופי
RUN yq --version && \
    helm version --short && \
    oc version --client && \
    argocd version --client && \
    gitlab-runner --version

# הגדרת תיקיית העבודה
WORKDIR /workspace

# פקודת ברירת מחדל
ENTRYPOINT ["/bin/bash"]
