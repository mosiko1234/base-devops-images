ARG BASE_IMAGE
FROM ${BASE_IMAGE}

USER root

# התקנת Python וכלים נדרשים
ARG PYTHON_VERSION

RUN echo "Using Python version: ${PYTHON_VERSION}" && \
    apt-get update && apt-get install -y --no-install-recommends \
    software-properties-common && \
    apt-key adv --keyserver keyserver.ubuntu.com --recv-keys BA6932366A755776 && \
    echo "deb http://ppa.launchpad.net/deadsnakes/ppa/ubuntu focal main" > /etc/apt/sources.list.d/deadsnakes.list && \
    apt-get update && apt-get install -y --no-install-recommends \
    python${PYTHON_VERSION} \
    python${PYTHON_VERSION}-venv \
    python${PYTHON_VERSION}-distutils \
    python3-pip \
    bash && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# עדכון pip
RUN python${PYTHON_VERSION} -m ensurepip && \
    python${PYTHON_VERSION} -m pip install --upgrade pip && \
    python${PYTHON_VERSION} -m pip install \
        virtualenv \
        pytest \
        pylint \
        black \
        flake8

# וידוא ש-Python פועל
RUN python${PYTHON_VERSION} --version

CMD ["python3"]
