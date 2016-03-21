# loxWidgets
Some useful utils and widgets for android development

**Blur image:**  
```java
RenderScriptManager rs = RenderScriptManager.create(context);  
Bitmap bitmap = rs.blurImage(image);  
rs.destroy();
```
  
**Cross-fade drawable:**  
```java
CrossfadeDrawable crossfader = new CrossfadeDrawable(fromBitmapDrawable); // fromBitmapDrawable is optional  
getRootView().setBackground(crossfader);  
crossfader.startTransition(300, toBitmapDrawable); // can be called multiple times  
```
