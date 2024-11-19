ARG BASE_IMAGE
FROM ${BASE_IMAGE}

USER root

# התקנת Python וכלים נדרשים
ARG PYTHON_VERSION

RUN apt-get update && apt-get install -y --no-install-recommends \
        python${PYTHON_VERSION} \
        python3-pip \
        python3-venv \
        && apt-get clean \
        && rm -rf /var/lib/apt/lists/*

# וידוא ש-Python פועל
RUN python${PYTHON_VERSION} --version

CMD ["python3"]
