# GameAndroidBase

Este projeto se encontra em atualização.

### Descrição

Este projeto tem como objetivo oferecer uma base de programação para o desenvolvimento de jogos para Android.

### Suporte a desenho:
- [X] Canvas
- [ ] OpenGL

### Funcionamento: **Boards** e **Scenes**
Basicamente, a engine possui um gerenciador de **Boards**, sendo que somente uma **Board**, por padrão, é carregada na memória por vez.

Cada board possui uma lista de **Scene (Cenas)**, que são usadas para dividir o contexto de uma **Board** em subcontextos.

![](Misc/basicWorking.png)

Exemplo: No menu principal do jogo, uma cena para cada menu, ou uma cena para uma animação rodando por trás da cena que cuida do menu.

![](Misc/basicWorking2.png)

### Uso: **Boards** e **Scenes**
Para uma nova **Board**, é necessario criar uma nova classe e estender a **Board**. Por padrão, use o construtor somente para inicializações leves, e no método **initBoard()** coloque o carregamento dos componentes a serem utilizados. (Cenas, texturas, etc.). No método **finalizeBoard()**, você deve liberar os recursos utilizados ou criados.
```java
public class MinhaBoard extends Board {

  public MinhaBoard(int x, int y, int alrgura, int altura) {
    super(x, y, largura, altura);
  }
  
  @Override
  public void initBoard() { }
  
  @Override
  public void finalizeBoard() { }

}
```

E para mostrar sua nova **Board**, deve-se ir na classe **GameEngine**, na função **initComponent()**, e na linha comentada com **START YOUR BOARD HERE**, faça uma chamada a **BoardManager.getInstance().post()**, e coloque a sua**Board** como parâmetro, fornecendo x e y para posição, e a largura e altura desejada.:

```java
Trecho de codigo acima omitido.
.......
     private void initComponents() {
        this.soundManager = new SoundManager(getContext());
        this.deviceManager = new DeviceManager();
        BoardManager.getInstance().set(this.deviceManager, this.soundManager);

        // --------------------------------
        // INICIE SUA BOARD AQUI
      
        BoardManager.getInstance().post(new MyBoard( posiçãoEmXdaBoard, posiçãoEmYdaBoard, larguraDaBoard, alturaDaBoard ));

        // --------------------------------        
    }
........
Trecho de codigo abaixo omitido.
```

Para uma nova cena, o processo é o mesmo, crie uma nova classe e estenda da **Scene**. Novamente, por padrão, use o construtor somente para inicializações leves, e no método **initScene()** coloque o carregamento dos componentes a serem utilizados. No método **finalizeScene()**, você deve liberar os recursos utilizados ou criados.

```java
public class MinhaScene extends Scene {

  public MinhaScene(int x, int y, int largura, int altura) {
    super(x, y, largura, altura);
  }
  
  @Override
  public void initScene() { }
  
  @Override
  public void finalizeScene() { }

}
```

Para adicionar a cena, defina a mesma na sua **Board** criada, e instancie ela na função **init()** da **Board**, fornecendo x e y para posição, e a largura e altura desejada. Após deve-se adicionar a cena na **Board** fazendo a chamada a **super.add()**. Só depois de adicionada, seus componentes serão inicializados e exibidos.

```java
public class MinhaBoard extends Board {

  Scene minhaScene;

  public MinhaBoard(int x, int y, int largura, int altura) {
    super(x, y, largura, altura);
  }
  
  @Override
  public void initBoard() {
    minhaScene = new Scene(posiçãoEmXdaCena, posiçãoEmYdaCena, larguraDaCena, alturaDaCena);
    super.add(minhaScene);
  }
  
  @Override
  public void finalizeBoard() { 
    super.finalizeBoard();
  }

}
```
### Funcionamento: **WindowEntity**
Uma **WindowEntity** é um componente utilizado em janelas, para criação de menus. Tipos atualmente inclusos:

| **Tipo** | **Descrição** |
| --- | --- |
| Button | Botão utilizado para lançar ações quando clicado. |
| Label | Campo utilizado para exibir alguma informação. |
| Table | Tabela utilizada para exibir informações em cascata. |
| TextField | Campo de texto utilizado para entrada de dados pelo usuário. |
| Window | Janela que agrega outras **WindowEntity**, utilizada para organizar os componentes.
