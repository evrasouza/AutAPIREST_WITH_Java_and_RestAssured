package br.com.automation.APIRest;

import static io.restassured.RestAssured.*;
import io.restassured.http.ContentType;
import static org.hamcrest.Matchers.*;
import org.junit.Test;


// https://dextra.com.br/pt/automacao-de-api-rest-utilizando-java-e-restassured/
// POST ESCRITO POR Daiane Macedo - Data de publicação: 25/01/2018 
public class TestAPI {
	
	// 1º Passo: Criar o método de teste que vai receber nosso bloco de instruções
	@Test
	public void Teste() {
		// 2º Passo: Definir nossa URI base para ser acessada
		String uriBase = "https://postman-echo.com/get";
		
		// 3º Passo: Como comentado acima, a nossa URI utiliza o protoloco HTTPS, para conseguirmos acessar uma URI https através do método, precisamos dizer para o nosso teste ignorar o uso do https e acessar a URI
		//Nota: Todas as configurações que precisamos fazer antes de enviar o nosso request (GET) devem ser passadas no nosso método given().
		given()
			.relaxedHTTPSValidation()
			// 4º Passo: Outro ponto que também comentamos anteriormente é que essa URI possui parametros, se você prestar atenção, vai perceber que nossa String uriBase, não possui os parametros foo1=bar1&foo2=bar2, eles devem ser definidos também no nosso given()
			.param("foo1", "bar1")
			.param("foo2", "bar2")
		// 5º Passo: Aqui já cuidamos de todos os pré requisitos para que o nosso request seja enviado, vamos então propriamente enviar a nossa requisição para o endereço definido:
			//Nota: O método when() é onde definimos a nossa ação, GET no caso – (Quando eu realizar um GET na uriBase) […]
		.when()
			.get(uriBase)
		// Passo 6: No passo 5 enviamos o request para nossa URI e nesse momento temos o retorno dela, então (then()) queremos validar algumas informações que voltaram nesse response, aqui entra a parte de validações que citei lá em cima:	
		.then()
			.statusCode(200)
			.contentType(ContentType.JSON)
			.body("headers.host", is("postman-echo.com"))
			.body("args.foo1", containsString("bar"));
	}
	
	/* Se pararmos para analisar as duas ultimas validações .body(“headers.host”, is(“postman-echo.com”)) e .body(“args.foo1”, containsString(“bar”)); vamos perceber que na primeira utilizamos a verificação is(“postman-echo.com”) que garante que o valor da chave “headers.host” é exatamente “postman-echo.com”.
	A segunda validação garante que a chave “args.foo1” possui a String “bar”, o retorno exato da chave “args.foo1” foi “bar1”, porém o metodo containsString() valida que o valor “bar1” possui a String “bar”, sendo isso verdadeiro o teste passa.
	Vamos então rodar nosso Teste e verificar o resultado:
	Botão Direito no método Teste() >> Run As >> Junit Test
	*/
	
	/*
	Podemos ver que nosso teste ficou verde indicando que passou 
	Agora para validarmos se ele realmente vai falhar se passarmos algo errado, vou alterar o trecho
	.body(“args.foo1”, containsString(“bar”)); para .body(“args.foo1”, containsString(“bar123”));
	E nosso teste vai ter que falhar, pois o valor da chave args.foo1 é bar1 é “bar1” e “bar1” não contém “bar123”
	*/
	
	@Test
	public void Teste_Erro() {
		String uriBase = "https://postman-echo.com/get";
		
		given()
			.relaxedHTTPSValidation()
			.param("foo1", "bar1")
			.param("foo2", "bar2")
		.when()
			.get(uriBase)
		.then()
			.statusCode(200)
			.contentType(ContentType.JSON)
			.body("headers.host", is("postman-echo.com"))
			.body("args.foo1", containsString("bar123"));
	}

}

/*
Falhou!
E o motivo foi este:
java.lang.AssertionError: 1 expectation failed.
JSON path args.foo1 doesn't match.
Expected: a string containing "bar123"
Actual: bar1
Passamos a String “bar123” mas a String contida na chave args.foo1 é “bar1”, logo nosso teste não passou. 
Bem pessoal, é isso, esse é um teste simples dentro da infinidade de coisas que dá para fazer utilizando RestAssured.
A parte que mais gosto da ferramenta é a possíbilidade de escrever os testes de API no formato BDD, deixa o código bem legivel e de fácil entendimento, permitindo que qualquer pessoa com conhecimento básico de Java crie testes rapidamente.
Links e Referências:
Download Postman https://www.getpostman.com/
Blog Postman http://blog.getpostman.com
Writing tests in Postman http://blog.getpostman.com/2017/10/25/writing-tests-in-postman/
Rest Status Code http://www.restapitutorial.com/httpstatuscodes.html
Hamcrest Validations https://code.google.com/archive/p/hamcrest/wikis/Tutorial.wiki
RestAssured Usage Guide https://github.com/rest-assured/rest-assured/wiki/Usage

*/