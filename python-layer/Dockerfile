ARG BASE_IMAGE
FROM ${BASE_IMAGE}
USER root
ARG PYTHON_VERSION

RUN echo "Using Python version: ${PYTHON_VERSION}" \
    && apt-get update && apt-get install -y --no-install-recommends \
    software-properties-common \
    && apt-key adv --keyserver keyserver.ubuntu.com --recv-keys BA6932366A755776 \
    && echo "deb http://ppa.launchpad.net/deadsnakes/ppa/ubuntu focal main" > /etc/apt/sources.list.d/deadsnakes.list \
    && apt-get update && apt-get install -y --no-install-recommends \
    python${PYTHON_VERSION} \
    python3-pip \
    python3-venv \
    && apt-get clean && rm -rf /var/lib/apt/lists/*
CMD ["python3"]