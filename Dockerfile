FROM ubuntu:latest
LABEL authors="dneychev"

ENTRYPOINT ["top", "-b"]