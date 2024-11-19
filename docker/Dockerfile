##
# This version of the Dockerfile is to be integrated with the Pipeline and it depends on
# the application being pre-packaged in the ./temp_libs directory at the repository
#
# (for reference, the commands below run at the repo root will prepare the dependencies)
# mvn install dependency:copy-dependencies
# mkdir ./docker/temp_libs && cp ./modules/web-app/target/architect-burgers-webapi-*.jar ./docker/temp_libs/ && cp ./modules/web-app/target/lib/* ./docker/temp_libs/
#
#
FROM eclipse-temurin:21-jdk-alpine

RUN adduser apprunner -D

RUN mkdir /app

COPY ../temp_libs/* /app/
COPY ./entrypoint.sh /app/

RUN chown -R apprunner /app
RUN chmod a+x /app/entrypoint.sh

USER apprunner

ENV archburgers.datasource-mongodb.uri=mongodb://localhost/techchallengedb
ENV archburgers.datasource-mongodb.database=techchallengedb

ENV archburgers.integration.mercadopago.apiBaseUrl=https://api.mercadopago.com
ENV archburgers.integration.mercadopago.userId=123456789
ENV archburgers.integration.mercadopago.accessToken=APP_USR-0000000000000000
ENV archburgers.integration.mercadopago.posId=LOJA1POS1
ENV archburgers.integration.mercadopago.notificationUrl=https://architectburgers-mplabs.requestcatcher.com/payment-hooks/mercadopago

ENV archburgers.integration.aws.region=us-east-1
ENV archburgers.integration.sqs.sqsEndpoint=http://localhost:4566
ENV archburgers.integration.sqs.pagamentosEmAbertoQueueName=pagamentosEmAberto
ENV archburgers.integration.sqs.pagamentosEmAbertoQueueUrl=http://localhost:4566/000000000000/pagamentosEmAberto
ENV archburgers.integration.sqs.pagamentosConcluidosQueueName=pagamentos_concluidos
ENV archburgers.integration.sqs.pagamentosConcluidosQueueUrl=http://localhost:4566/000000000000/pagamentos_concluidos

EXPOSE 8090

CMD ["/app/entrypoint.sh"]
