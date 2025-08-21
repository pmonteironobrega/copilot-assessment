
<h1>Contexto</h1>

O projeto solicitou a criação de uma API de consulta em massa para prover 
informações a um aplicativo. 
Esta API deve retornar os produtos indisponiveis em uma loja - ou lista de lojas.

1. **Endpoint da API** O backend deverá fornecer um endpoint de API para que o sistema RAP possa solicitar informações sobre itens indisponíveis em locais específicos.
2. **Parâmetros de Entrada** ==O endpoint deverá aceitar os seguintes parâmetros na requisição:== ```json { "restaurantBrand": "A marca do restaurante para a qual a consulta é feita. ", "location": "O(s) local(is) específico(s) dentro da marca do restaurante. O sistema deve ser capaz de lidar com um ou múltiplos locais em uma única chamada. " } ``` 
3. **Funcionalidades** 
   - Seleção de Restaurante e Localização: O sistema deve ser capaz de filtrar os dados com base no restaurante e na localização fornecidos. 
   - Recuperação de Dados: Ao receber uma requisição, o backend consulta o sistema IDP para obter a lista de itens indisponíveis para a combinação de marca e local(is) especificada(s). 
   - Processamento em Massa: O backend deve ser otimizado para lidar com requisições contendo um grande volume de locais simultaneamente, garantindo um desempenho ideal. 
4. Formato da Resposta A API responderá com um objeto JSON. 

>Em caso de sucesso: A resposta conterá uma lista de itens indisponíveis.
 
>Em caso de erro: A resposta conterá uma mensagem de erro apropriada que descreva o problema ocorrido durante a recuperação dos dados.