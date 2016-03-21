# loxWidgets
Some useful utils and widgets for android development

**Blur image:**  
```java
RenderScriptManager rs = RenderScriptManager.create(context);  
Bitmap bitmap = rs.blurImage(image);  
rs.destroy();
```
  
**Cross-fade drawable:**  
Start with drawable or transparent view and cross-fade from last state to next given drawable.  
Can be used for slideshows or combined with image blurring for frosty glass effect action in the background.
```java
CrossfadeDrawable crossfader = new CrossfadeDrawable(fromDrawable); // fromBitmapDrawable is optional  
getRootView().setBackground(crossfader);  
crossfader.startTransition(300, toBitmapDrawable); // can be called multiple times  
```
