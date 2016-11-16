# Simple POS #

Este exemplo foi criado para demonstrar a integração Order Manager Sdk com um app simples. 

O app foi desenvolvido utilizando conceitos da arquitetura MVP ("Model View Presenter"), onde as funcionalidades do SDK estão concentradas na classe PaymentManager.

### Setup ###

Para utilizar o exemplo é necessário adicionar as informações de Access Key ID e Secret Access Key no arquivo gradle.properties, que fica localizado na pasta '.gradle' dentro do home do usuário (~). 

Após obter a chave no portal da Cielo, basta colar as linhas abaixo no gradle.properties:

AccessKeyId="SUA_ACCESS_KEY_ID"

sSecretAccessKey="SUA_SECRET_ACCESS_KEY"
