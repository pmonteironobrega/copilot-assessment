Os prompts foram construídos no modo agent utilizando o modelo GPT-4.1. Todas as modificações realizadas pelo agent foram feitos a partir de um projeto spring boot com kotlin e gradle configurado somente com o básico

<h2>Prompt inicial</h2>

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
    Aparentemente não houve clareza na construção do prompt pois apesar do codigo ter sido gerado corretamente não foram adicionadas as dependencias para o funcionamento da API.
    Deve-se adicionar detalhes mais claros. 
<h2>Adição do atributo location</h2>
O prompt inicial não compreendeu no modelo de dados o atributo location então foi necessária mais uma interação

```text
      Modifique o a classe Store e adicione o atributo location 
      Modifique os endpoints para aceitarem como input o nome da loja e a localizacao      
```
  A construção do prompt acima foi feito com o intutio de modificar o endpoint e realizar as alterações nos services chamados pelo controller. Porem, não houveram informações suficientes no prompt e houveram erros na       geração do codigo conforme a saida abaixo gerada

```text  
As modificações foram realizadas:
O atributo location foi adicionado à classe Store.
O repositório agora permite buscas por nome e localização.
O serviço e o controller foram ajustados para aceitar nome e localização como input nos endpoints.
Para buscar produtos não disponíveis de uma loja, use GET /stores/unavailable-products?name=NOME&location=LOCALIZACAO.
Para múltiplas lojas, envie uma lista de objetos {"name": ..., "location": ...} via POST para /stores/unavailable-products/multi.
```
