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
<h2>Modificação da obrigatoriedade dos campos e tratamento dos inputs</h2>
<hr>
Para que não seja obrigatorio o envio do estabelecimento e da localização para retorno dos dados e para que seja possível o envio de dados separados por espaço, fiz o prompt abaixo

```text  
Modifique os endpoints para que não seja obrigatorio o envio de name e location. 
Modifique os enpoints para que interprete valores como no exemplo abaixo Loja+A
E realize a busca considerando que o + significa um espaço
```

No prompt, solicitei que as alterações fossem realizadas nos enpoints mas não deixei claro sobre qualquer modificação no service. O copilot gerou uma função nova para ser chamada dentro do endpoint mas não realizou qualquer implementação dentro do StoreService o que exigiu uma modificação

```text  
Modifique o storeService e implemente a nova função inserida
```
E também foi solicitada a troca do nome dos atributos para seguir o definido nos requisitos

```text  
Modifique os parametros de entrada do controller para o seguinte name = restaurantBrand location = location Modifique os servicos e repositories associados
Modifique tambem o document para que name seja renomeado para restaurantBrand
```

<h2>Tratamento de erros</h2>
<hr>

Prompt para gerar as modificações na API para gerar uma mensagem de erro, caso a busca não tenha sucesso

```text
Os endpoints /unavailable-products e /unavailable-products/multi devem ter o seguinte comportamento: a. Em caso de sucesso, deve retornar a lista de produtos faltantes naquela store b. Em caso de erro, deve retornar uma mensagem de erro que descreva o problema ocorrido O caso de erro deve cobrir diferentes tipos de exceção e deve ser de forma centralizada para que nao aja duplicação de codigo. As exceptions devem ser lançadas pelo service e apresentada pelo controller. Caso não existam registros que correspondam a query, deve-se retornar um erro 404 com a mensem: "Nenhum registro encontrado" Centralize as mensagens retornadas em um enum Retorne a mensagem pro usuario via ResponseEntity.status
Modifique o controller e service com as alterações necessárias. Certifique-se que o codigo esteja otimizado para que não sejam geradas duplicações como no exemplo abaixo if (restaurantBrand == null && location == null) {
return getAllUnavailableProducts().values.flatten()
}
if (restaurantBrand != null &&
restaurantBrand.isBlank()) {
throw IllegalArgumentException("O parâmetro restaurantBrand não pode ser vazio.")
}

```
Esta versão foi especifica suficiente pra gerar todos os requisitos solicitados.




