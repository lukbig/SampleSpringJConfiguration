FROM postgres:10.1

ADD ./init.sql /docker-entrypoint-initdb.d/

# Set the locale
RUN sed -i -e 's/# pl_PL.UTF-8 UTF-8/pl_PL.UTF-8 UTF-8/' /etc/locale.gen && \
    locale-gen
ENV LANG pl_PL.UTF-8  
ENV LANGUAGE pl_PL:pl
ENV LC_ALL pl_PL.UTF-8

ENV POSTGRES_PASSWORD=test
ENV POSTGRES_USER=test
ENV POSTGRES_DB=test-db-name