package br.com.automation.APIRest;

import static io.restassured.RestAssured.*;
import io.restassured.http.ContentType;
import static org.hamcrest.Matchers.*;
import org.junit.Test;


// https://dextra.com.br/pt/automacao-de-api-rest-utilizando-java-e-restassured/
// POST ESCRITO POR Daiane Macedo - Data de publica��o: 25/01/2018 
public class TestAPI {
	
	// 1� Passo: Criar o m�todo de teste que vai receber nosso bloco de instru��es
	@Test
	public void Teste() {
		// 2� Passo: Definir nossa URI base para ser acessada
		String uriBase = "https://postman-echo.com/get";
		
		// 3� Passo: Como comentado acima, a nossa URI utiliza o protoloco HTTPS, para conseguirmos acessar uma URI https atrav�s do m�todo, precisamos dizer para o nosso teste ignorar o uso do https e acessar a URI
		//Nota: Todas as configura��es que precisamos fazer antes de enviar o nosso request (GET) devem ser passadas no nosso m�todo given().
		given()
			.relaxedHTTPSValidation()
			// 4� Passo: Outro ponto que tamb�m comentamos anteriormente � que essa URI possui parametros, se voc� prestar aten��o, vai perceber que nossa String uriBase, n�o possui os parametros foo1=bar1&foo2=bar2, eles devem ser definidos tamb�m no nosso given()
			.param("foo1", "bar1")
			.param("foo2", "bar2")
		// 5� Passo: Aqui j� cuidamos de todos os pr� requisitos para que o nosso request seja enviado, vamos ent�o propriamente enviar a nossa requisi��o para o endere�o definido:
			//Nota: O m�todo when() � onde definimos a nossa a��o, GET no caso � (Quando eu realizar um GET na uriBase) [�]
		.when()
			.get(uriBase)
		// Passo 6: No passo 5 enviamos o request para nossa URI e nesse momento temos o retorno dela, ent�o (then()) queremos validar algumas informa��es que voltaram nesse response, aqui entra a parte de valida��es que citei l� em cima:	
		.then()
			.statusCode(200)
			.contentType(ContentType.JSON)
			.body("headers.host", is("postman-echo.com"))
			.body("args.foo1", containsString("bar"));
	}
	
	/* Se pararmos para analisar as duas ultimas valida��es .body(�headers.host�, is(�postman-echo.com�)) e .body(�args.foo1�, containsString(�bar�)); vamos perceber que na primeira utilizamos a verifica��o is(�postman-echo.com�) que garante que o valor da chave �headers.host� � exatamente �postman-echo.com�.
	A segunda valida��o garante que a chave �args.foo1� possui a String �bar�, o retorno exato da chave �args.foo1� foi �bar1�, por�m o metodo containsString() valida que o valor �bar1� possui a String �bar�, sendo isso verdadeiro o teste passa.
	Vamos ent�o rodar nosso Teste e verificar o resultado:
	Bot�o Direito no m�todo Teste() >> Run As >> Junit Test
	*/
	
	/*
	Podemos ver que nosso teste ficou verde indicando que passou 
	Agora para validarmos se ele realmente vai falhar se passarmos algo errado, vou alterar o trecho
	.body(�args.foo1�, containsString(�bar�)); para .body(�args.foo1�, containsString(�bar123�));
	E nosso teste vai ter que falhar, pois o valor da chave args.foo1 � bar1 � �bar1� e �bar1� n�o cont�m �bar123�
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
Passamos a String �bar123� mas a String contida na chave args.foo1 � �bar1�, logo nosso teste n�o passou. 
Bem pessoal, � isso, esse � um teste simples dentro da infinidade de coisas que d� para fazer utilizando RestAssured.
A parte que mais gosto da ferramenta � a poss�bilidade de escrever os testes de API no formato BDD, deixa o c�digo bem legivel e de f�cil entendimento, permitindo que qualquer pessoa com conhecimento b�sico de Java crie testes rapidamente.
Links e Refer�ncias:
Download Postman https://www.getpostman.com/
Blog Postman http://blog.getpostman.com
Writing tests in Postman http://blog.getpostman.com/2017/10/25/writing-tests-in-postman/
Rest Status Code http://www.restapitutorial.com/httpstatuscodes.html
Hamcrest Validations https://code.google.com/archive/p/hamcrest/wikis/Tutorial.wiki
RestAssured Usage Guide https://github.com/rest-assured/rest-assured/wiki/Usage

*/