# Documentação da API - Sistema de Consulta de Produtos Indisponíveis

## Endpoints

### 1. Buscar produtos indisponíveis por loja
**GET** `/stores/unavailable-products`

**Parâmetros de entrada (query):**
- `restaurantBrand` (opcional): Nome da marca/restaurante
- `location` (opcional): Localização da loja

**Response de sucesso:**
```json
{
  "results": [
    { "name": "Produto 1", "qty": 0 },
    { "name": "Produto 2", "qty": 0 }
  ]
}
```

**Response de erro:**
- Parâmetro inválido:
```json
{
  "error": "O parâmetro restaurantBrand não pode ser vazio."
}
```
- Nenhum registro encontrado:
```json
{
  "error": "Nenhum registro encontrado"
}
```
- Erro interno:
```json
{
  "error": "Erro interno ao buscar produtos indisponíveis"
}
```

---

### 2. Buscar produtos indisponíveis por múltiplas lojas
**POST** `/stores/unavailable-products/multi`

**Body (JSON):**
```json
[
  { "restaurantBrand": "Restaurante A", "location": "Centro" },
  { "restaurantBrand": "Restaurante B", "location": "Zona Sul" }
]
```

**Response de sucesso:**
```json
{
  "results": [
    { "name": "Produto 1", "qty": 0 },
    { "name": "Produto 2", "qty": 0 }
  ]
}
```

**Response de erro:**
- Body vazio:
```json
{
  "error": "A lista de stores não pode ser vazia."
}
```
- Nenhum registro encontrado:
```json
{
  "error": "Nenhum registro encontrado"
}
```
- Erro interno:
```json
{
  "error": "Erro interno ao buscar produtos indisponíveis"
}
```

---

## Service: StoreService

### Funções implementadas

#### 1. `getUnavailableProductsByStore(restaurantBrand: String?, location: String?): Map<String, List<Product>>`
- **Utilidade:** Busca todos os produtos indisponíveis (qty = 0) de uma loja específica, filtrando por marca e/ou localização. Se ambos forem nulos, retorna de todas as lojas.
- **Onde é chamada:** Endpoint GET `/stores/unavailable-products` e internamente pelo método de múltiplas lojas.
- **Responses possíveis:**
  - Sucesso: `{ "results": [ ...produtos... ] }`
  - Parâmetro inválido: lança `IllegalArgumentException` (400)
  - Nenhum registro encontrado: lança `NoSuchElementException` (404)
  - Erro interno: lança `Exception` (500)

#### 2. `getUnavailableProductsByStores(requests: List<StoreQueryRequest>): Map<String, List<Product>>`
- **Utilidade:** Busca todos os produtos indisponíveis (qty = 0) de múltiplas lojas, agregando todos os resultados em uma única lista.
- **Onde é chamada:** Endpoint POST `/stores/unavailable-products/multi`
- **Responses possíveis:**
  - Sucesso: `{ "results": [ ...produtos... ] }`
  - Body vazio: lança `IllegalArgumentException` (400)
  - Nenhum registro encontrado: lança `NoSuchElementException` (404)
  - Erro interno: lança `Exception` (500)

#### 3. `getAllUnavailableProducts(): Map<String, List<Product>>`
- **Utilidade:** Busca todos os produtos indisponíveis de todas as lojas, agrupando por loja.
- **Onde é chamada:** Internamente pelo método de busca por loja quando não há filtros.
- **Responses possíveis:**
  - Sucesso: `{ "Loja A - Centro": [ ...produtos... ], ... }`

#### 4. `saveStore(store: Store): Store`
- **Utilidade:** Salva uma loja no banco de dados.
- **Onde é chamada:** Endpoint POST `/stores`
- **Responses possíveis:**
  - Sucesso: retorna o objeto Store salvo

---

## Implementação do Código

- O controller centraliza o tratamento de exceções usando `@RestControllerAdvice`, garantindo que todas as mensagens de erro sejam padronizadas e retornadas via `ResponseEntity.status`.
- As mensagens de erro são centralizadas no enum `StoreErrorMessage`, facilitando manutenção e padronização.
- O service realiza todas as validações e lança exceções conforme o tipo de erro (parâmetro inválido, registro não encontrado, erro interno).
- O padrão de resposta dos endpoints é sempre `{ "results": [...] }`, tanto para uma loja quanto para múltiplas lojas, facilitando o consumo da API.
- O código está otimizado para evitar duplicação, utilizando funções auxiliares e validações centralizadas.
- O acesso ao MongoDB é feito via Spring Data, utilizando repositórios customizados para busca por marca e localização.

---

## Observações
- Todos os endpoints retornam status HTTP apropriados (200, 400, 404, 500) conforme o tipo de resposta.
- O padrão de resposta facilita integração com frontends e outros sistemas.
- O projeto está preparado para evoluções, como paginação ou filtros adicionais.

