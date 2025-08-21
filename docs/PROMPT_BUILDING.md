<h2>Construção do prompt</h2>

Os prompts foram construídos no modo agent utilizando o modelo GPT-4.1. Todas as modificações realizadas pelo agent foram feitos a partir de um projeto spring boot com kotlin e gradle configurado somente com o básico

1. Processo de construção dos prompts
   a. Prompt inicial
      ```text
      Atue como um especialista em kotlin com spring boot.
      Contexto
      Preciso construir uma API para buscar itens não disponiveis em determinadas lojas. Esta API deve ter a capacidade de retornar os itens não disponiveis a partir de uma loja ou por multiplas lojas
      Tarefas
      Modifique o projeto dado no contexto para que ele esteja preparado pra prover estas informações a partir deste request
      Configure o projeto para utilizar o mongodb como base de dados. 
      Crie os documents para insercao e recuperacao dos dados
      Modelo
      {  
        "store": {  
          "name": "STORE_NAME",  
          "products": [  
            {  
              "name": "PRODUCT_NAME",  
              "qty": "total_products"  
            }  
          ]  
        }  
      }
      Dados de configuracao do mongo
      URI: <MONGO_URI>
      Database: rapsystem
      Crie os repositories para acesso das funções de listar e armazenar dados
      
      ```
   Output
      ```text
      Para preparar o projeto Kotlin com Spring Boot para prover uma API que retorna itens não disponíveis em lojas, e configurar o MongoDB como base de dados, seguirei estes passos:
         1. Adicionar dependências do MongoDB no build.gradle.kts.
         2. Configurar o acesso ao MongoDB em application.properties.
         3. Criar os documentos (Store e Product) para persistência.
         4. Criar os repositórios para listar e armazenar dados.
         5. Preparar a estrutura para a API de consulta.
      ```
