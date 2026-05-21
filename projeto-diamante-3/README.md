# Decisões de Projeto
##### Tarefa 3 — AssessmentTools
- As ferramentas de criação e atualização de avaliações foram separadas em uma classe específica (AssessmentTools), mantendo a lógica fora da interface e evitando acesso direto ao repository.

- A implementação reutiliza o AssessmentService, pra focar na persistência e evitar duplicação do código. 

#### Decisão adotada
- separação das tools em serviço próprio;
- reutilização da camada service;
- uso de Builder para criação da entidade;
- atualização simples e objetiva da avaliação.

#### Potencial melhoria
A atualização da avaliação percorre toda a lista de avaliações para localizar um único registro com o getAllAssessments().stream()

Um uso melhor seria adicionar um método findById() no service para buscar direto pelo ID.

##### Tarefa 4 — ShellToolsService
- A ferramenta de clonagem de repositórios foi implementada utilizando ProcessBuilder, permitindo executar o comando git clone

#### Decisões adotadas
- uso de ProcessBuilder ao invés de Runtime.exec;
- criação automática do diretório base de repositórios;
- separação da lógica de extração do nome do repositório;
- tratamento básico de erros e código de saída do processo.

#### Potencial melhoria
Separar o tratamento de InterruptedException
IOException para aplicar Thread.currentThread().interrupt() apenas em casos de interrupção da thread.

##### Tarefa 5 — FileSystemToolsService
As ferramentas de sistema de arquivos foram separadas em dois comportamentos principais:

- listagem recursiva de arquivos;
- leitura de conteúdo de arquivos.

#### Decisões adotadas

- responsabilidade única para cada método;
- recursividade isolada em método auxiliar;
- uso de Files.readString() para simplificar leitura de arquivos;
- retorno de caminhos relativos para facilitar navegação do agente.
#### Potencial melhoria

Adicionar validação de caminhos utilizando Path.normalize() para impedir acessos inválidos com caminhos relativos como:

../../arquivo

##### Tarefa 6 — Skills
O prompt de correção ficou em Markdown (project-review-skill.md), desacoplando isso do código Java.

#### Decisões adotadas
- armazenamento da skill em arquivo externo;
- carregamento via ClassPathResource;
- instruções curtas e objetivas;
- definição clara das responsabilidades do agente.

O objetivo foi evitar overengineering no projeto, usando boas práticas e simplicidade.