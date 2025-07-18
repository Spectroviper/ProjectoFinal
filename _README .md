
# 📱 Aplicativo de Gerenciamento de Desafios e Jogos

Este é um projeto mobile desenvolvido em **Kotlin com Jetpack Compose**, com backend em **Node.js** e banco de dados **MySQL**. A aplicação permite gerenciar jogos, criar desafios, acompanhar conquistas e mais.

---

## ✅ Pré-requisitos

Antes de comecar, é preciso ter os seguintes componentes instalados:

### 🔧 Ambiente de Desenvolvimento

- [Android Studio](https://developer.android.com/studio) (AGP 8.1.0) com SDK 36
- [Node.js](https://nodejs.org/en) (versão mais recente)
- [Docker](https://www.docker.com/)
- Git (opcional)
- MySQL workbench (opcional)

---

## ⚙️ Instalação e Configuração

### 1. 📦 Backend (API)

1. Faca download do projecto.
2. Vá até a pasta do servidor:
   ```bash
   cd ./API
3. Para correr no ambiente de produção executar no terminal:
   ```
   docker-compose -f docker-compose.prod.yml up --build   

   ```
4. Para correr no ambiente de desenvolvimento executar no terminal:
   ```
   docker-compose -f docker-compose.yml up --build   

   ```
5. Para popular as entidades com Data no ambiente de producao executar noutro terminal enquanto o terminal inicial da API corre:
   ```
   docker exec -it backend_prod npm run seed
   ```
6. Para popular as entidades com Data no ambiente de desenvolvimento executar noutro terminal enquanto o terminal inicial da API corre:
   ```
   docker exec -it backend npm run seed
   ```

⚠️ Para modificar os dados relativamente a coneção da API a base de dados aceder ao ficheiro .env (este tem informação importante relativamente a instancia MYSQL que a API espera estar a ser ligada com).

### 2. 🛢️ Banco de Dados (MySQL)

- Instale uma instância do MySQL ou use Docker.
- Configure o ficheiro .env como achar melhor, por default bem assim:
   **MYSQL_USER**=admin
   **MYSQL_PASSWORD**=admin1234
   **MYSQL_ROOT_PASSWORD**=admin1234
   **MYSQL_DATABASE**=admin
   **MYSQL_PORT**=3306
   **DB_HOST**=mysql

   **SERVER_PORT**=3000

> Certifique-se de que o Docker está a correr para a API e a base de dados funcionarem corretamente.

### 3. 🤖 App Android

1. Abra o projeto no Android Studio.
2. Configure o IP da conexão no arquivo:

   ```
   ProjectoFinal\03_Implementacao\ProjectTestFinal\app\src\main\java\com\example\projecttest\AppConfig.kt
   ```

   Altere o IP para apontar para o IP da máquina onde o backend está a correr. Se correr no seu proprio computador o IP sera o IPV4 que obtiver quando fizer o comando **ipconfig**

3. No path seguinte tera tambem de meter o IP associado a API (o mesmo que no passo anterior) para mermitir conecoes para e de esse IP:
   ```
   ProjectoFinal\03_Implementacao\ProjectTestFinal\app\src\main\res\xml\network_security_config.xml
   ```

   Verifique se as permissões estão configuradas para permitir conexões não seguras (HTTP).

4. Crie e configure um emulador Android (AVD) com API nível 33+, preferencia 35.

---

## 🚀 Execução

1. Conecte um dispositivo Android ou inicie o emulador.
2. Clique em **Run** ou use o Gradle:
   ```bash
   ./gradlew installDebug
   ```

A aplicação iniciará no ecrã de login. Depois isso, poderá:

- Criar e editar jogos.
- Criar e iniciar desafios.
- Verificar users e conquistas.
- Usar navegação entre tabs e perfis.

---

## 📶 Estrutura de Conexão

- A aplicação conecta-se a uma **API GraphQL** usando Apollo Client.
- Certifique-se de que a porta e IP estão corretos.
- Em ambientes de emulador Android, use `10.0.2.2` em vez de `localhost` para acessar a máquina host (se o quiser fazer).

---

## 🧪 Dados de Teste

Para testar a app o utilizador tem que criar um user para poder navegar na app a partir do signUp.
