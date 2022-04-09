# Palestra Spring Security

## Vantagens

1. Além das especificações
2. Portável
3. Altamente customizável
4. Protege contra os ataques mais conhecidos

## Tópicos
1. [Login com HTTP Basic](#login-com-http-basic)
2. [Login com página HTML gerada pelo Spring Security](#login-com-página-html-gerada-pelo-spring-security)
3. [Login com página customizada](#login-com-página-customizada)
4. [Proteger páginas do sistema e configurar permissão para usuários](#proteger-páginas-do-sistema-e-configurar-permissão-para-usuários)
5. [Desabilitar itens sem permissão do usuário](#desabilitar-itens-sem-permissão-do-usuário)
6. [Incluir o botão de sair](#incluir-o-botão-de-sair)
7. [Buscando usuário da base de dados](#buscando-usuário-da-base-de-dados)
8. [Implementação do UserDetails](#implementação-do-userdetails)
9. [Exibindo o nome do usuário na página html](#exibindo-o-nome-do-usuario-na-pagina-html)
10. [Configurando o "remember-me"](#configurando-o-remember-me)


### Login com HTTP Basic
- Nesse ponto, login com HTTP Basic, ao adicionar a dependência do Spring Security a cada inicialização é gerado uma senha e um username user definido por padrão pelo Spring Security.
### Login com página HTML gerada pelo Spring Security
   ```java
    @Configuration
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest()
                    .authenticated().and().formLogin();
        }
    
    }
   ```
 - Dentro da classe do `WebSecurityConfig` o método `anyRequest()` dentro do metodo `configure()` faz com que todas as requisições, feitas a aplicação, o usuário esteja autenticado. E solicitamos ao Spring Security que essa autenticação seja feita através do `formLogin()`.
### Login com página customizada
  ```java
      @Configuration
      public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

          @Override
          protected void configure(HttpSecurity http) throws Exception {
              http.authorizeRequests().anyRequest()
                      .authenticated().and().formLogin()
                        .loginPage("/entrar").permitAll();
          }

      }
   ```    
- Após criar um template HTML de login e defini-lo com a requisição `/entrar` dentro do controller, na classe `WebSecurityConfig` o método `.loginPage()` diz ao spring o template HTML responsável por fazer o login.

### Proteger páginas do sistema e configurar permissão para usuários
   ```java
      @Configuration
      public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

          @Override
          protected void configure(HttpSecurity http) throws Exception {
              http.authorizeRequests()
                      .antMatchers("/projetos").hasAnyRole("PG_PROJETOS")
                      .antMatchers("/relatorio-equipe").hasAnyRole("PG_REL_EQUIPE")
                      .antMatchers("/relatorio-custos").hasAnyRole("PG_REL_CUSTOS")
                      .anyRequest()
                      .authenticated()
                  .and().formLogin()
                      .loginPage("/entrar").permitAll();
          }

      }
   ```
  - Através do método `.antMatchers()`, conseguimos passar as URI que o controller em questão possui e atribuimos a ela quais Roles podem acessar através do método `hasAnyRole()`. Nesse ponto conseguimos proteger nossas páginas.
    ```java
    @Configuration
    public class InMemorySecurityConfig {
    
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
            builder
                    .inMemoryAuthentication()
                    .passwordEncoder(NoOpPasswordEncoder.getInstance())
                    .withUser("ze").password("132").roles("PG_PROJETOS", "PG_REL_CUSTOS", "PG_REL_EQUIPE")
                    .and()
                    .withUser("bia").password("143").roles("PG_PROJETOS", "PG_REL_EQUIPE");
        }
    }
    ```  
    - Como ainda não persistimos nossos usuários no banco de dados, trabalhamos com eles em memoria. Esse metodo, poderia estar dentro de qualquer classe de coniguração, mas, pela regra de negócio atribumos essa implementação a outra classe, `InMemorySecurityConfig`
    - Com os métodos `withUser()` e `password()` definimos o username e senha, respectivamente, como também através do método `roles()` configuramos as permissões do usuário com as roles a qual possui.
    
    **OBS**: o método `.passwordEncoder(NoOpPasswordEncoder.getInstance())` precisa ser definido na aplicação, se não apresentará erro. 
    
    [Referência de solução com erro de password](https://www.yawintutor.com/illegalargumentexception-there-is-no-passwordencoder-mapped-for-the-id-null/#:~:text=The%20exception%20%E2%80%9C%20java.lang.IllegalArgumentException%3A%20There%20is%20no%20PasswordEncoder,the%20password%20to%20be%20used%20as%20plain%20text.)
### Desabilitar itens sem permissão para o usuário
  - Após configurar as roles dos usuários, desabilitamos as páginas a qual ele não possui acesso, através do atributo `sec:authorize="hasRole('PG_PROJETOS')”`. Para isso, precisamos da depencia  `thymeleaf-extras-springsecurity5`.
    
    ```html
    <div id="navbar" class="collapse navbar-collapse">
    	<ul class="nav navbar-nav">
    		<li sec:authorize="hasRole('PG_PROJETOS')"><a href="/projetos">Projetos</a></li>
        <li sec:authorize="hasRole('PG_REL_CUSTOS')"><a href="/relatorio-custos">Relatório Custos</a></li>
        <li sec:authorize="hasRole('PG_REL_EQUIPE')"><a href="/relatorio-equipe">Relatório Equipe</a></li>
      </ul>     
    </div>
    ```
### Incluir o botão de sair
  - Implementamos um form com a action `/logout` e method `post` que é recebido pela classe `WebSecurityConfig`. Nessa classe usamos o metodo `logout()` para desconectar o usuário, como também damos acesso a ele através do `logoutSuccessUrl()`. Ficando da seguinte forma:
    
    ```java
    @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/projetos").hasAnyRole("PG_PROJETOS")
                    .antMatchers("/relatorio-equipe").hasAnyRole("PG_REL_EQUIPE")
                    .antMatchers("/relatorio-custos").hasAnyRole("PG_REL_CUSTOS")
                    .anyRequest()
                    .authenticated()
                    .and().formLogin()
                    .loginPage("/entrar").permitAll()
                    .and().logout().logoutSuccessUrl("/entrar?logout").permitAll();
        }
    ```
### Buscando usuário da base de dados
   - A busca do usuário na base de dados é feita através do `UserDetailsService` que devolve os dados do usuário, recuperados pelo `UserRepository`, para o `UserDetail`, como no código a seguir:
    
  ```java
    @Service
    public class UserDetailService implements UserDetailsService {
    
        @Autowired
        private UserRepository userRepository;
    
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            User user = this.userRepository.findByUsername(username);
            UserDetail detail = new UserDetail(user);
            return detail;
        }
    }
  ```
### Implementação do UserDetails
  - Ao implementar a interface `UserDetails` ela trás consigo métodos com parâmetros vindos da classe `userDetailsService`, que recupera os dados do usuário.

### Exibindo o nome do usuário na página html
  - Usando a integração do Thymeleaf com o Spring Security, o trecho de código,`principal.username` devolve o atributo `username` da  implementação de `UserDetails` , que neste caso retorna o nome do usuário.
    
    ```html
    <div class="page-header">
    	<h1><span sec:authentication="principal.username"></span>, Bem vindo(a) ao sistema!</h1>
    </div>
    ```
### Configurando o "remember-me"
  - Para que o Spring Security recarregue os dados do usuário através da função `rememberMe()`, é feito o uso da classe `userDetailsService` que faz a busca no banco de dados por meio da interface `userRepository`. Implementação desse método a seguir:
    
    ```java
    @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/projetos").hasAnyRole("PG_PROJETOS")
                    .antMatchers("/relatorio-equipe").hasAnyRole("PG_REL_EQUIPE")
                    .antMatchers("/relatorio-custos").hasAnyRole("PG_REL_CUSTOS")
                    .anyRequest()
                    .authenticated()
                    .and().formLogin()
                        .loginPage("/entrar")
                        .permitAll()
                    .and().logout()
                        .logoutSuccessUrl("/entrar?logout")
                        .permitAll()
                    .and().rememberMe()
                        .userDetailsService(userDetailService);
        }
    ```
    

## Referências

[Spring Security: Database-backend UserDetailsService](https://www.baeldung.com/spring-security-authentication-with-a-database)

[JDBC Authentication](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/jdbc.html)
