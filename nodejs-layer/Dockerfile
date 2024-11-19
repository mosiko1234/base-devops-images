## Use base image
#ARG BASE_IMAGE
#FROM ${BASE_IMAGE}
#
## Set environment variables
#ENV DEBIAN_FRONTEND=noninteractive
#
## Accept Node.js version as an argument
#ARG NODE_VERSION
#
## Install Node.js and npm dynamically
#RUN apt-get update && \
#    curl -fsSL https://deb.nodesource.com/setup_${NODE_VERSION}.x | bash - && \
#    apt-get install -y --no-install-recommends \
#        nodejs && \
#    apt-get clean && \
#    rm -rf /var/lib/apt/lists/*
#
## Verify installation
#RUN node -v && npm -v
#
## Set default entrypoint
#ENTRYPOINT ["/bin/bash", "-c"]
#CMD ["node -v"]
