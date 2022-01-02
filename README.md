<h1> 
    <a href="https://magician-io.com">Magician-Web</a> ·
    <img src="https://img.shields.io/badge/licenes-MIT-brightgreen.svg"/>
    <img src="https://img.shields.io/badge/jdk-11+-brightgreen.svg"/>
    <img src="https://img.shields.io/badge/maven-3.5.4+-brightgreen.svg"/>
    <img src="https://img.shields.io/badge/release-master-brightgreen.svg"/>
</h1>

Magician-Web is the official web component of Magician, which can easily manage Controllers, support interceptors, session management, annotated parameter validation, entity class receiving parameters, etc.

## Documentation

[https://magician-io.com](https://magician-io.com)

## Example

### Importing dependencies

```xml
<!-- This is the jar package build by this project -->
<dependency>
    <groupId>com.github.yuyenews</groupId>
    <artifactId>Magician-Web</artifactId>
    <version>2.0.1</version>
</dependency>

<!-- This is Magician -->
<dependency>
    <groupId>com.github.yuyenews</groupId>
    <artifactId>Magician</artifactId>
    <version>2.0.1</version>
</dependency>

<!-- This is the log package, which supports any package that can be bridged with slf4j -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-jdk14</artifactId>
    <version>1.7.12</version>
</dependency>
```

### Creating core handlers

```java
@HttpHandler(path="/")
public class DemoHandler implements HttpBaseHandler {

    @Override
    public void request(MagicianRequest magicianRequest, MagicianResponse response) {
        try{
            MagicianWeb.createWeb()
                    .request(magicianRequest);
        } catch (Exception e){
        }
    }
}
```

### Creating Controller

```java
@Route("/demoController")
public class DemoController {

	// You can use entity classes to receive parameters
	@Route(value = "/demo", requestMethod = ReqMethod.POST)
	public DemoVO demo(DemoVO demoVO){
		return demoVO;
	}

	// You can also directly use MagicianRequest to get parameters
	@Route(value = "/demob", requestMethod = ReqMethod.POST)
	public String demob(MagicianRequest request){
		return "ok";
	}

	// Download file
	@Route(value = "/demob", requestMethod = ReqMethod.POST)
	public ResponseInputStream demob(){
		ResponseInputStream responseInputStream = new ResponseInputStream();
		responseInputStream.setName("file name");
		responseInputStream.setBytes(file bytes);
		return responseInputStream;
	}
}
```


### Start HTTP service

```java
Magician.createHttp()
                .scan("handler和controller所在的包名")
                .bind(8080);
```