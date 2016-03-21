# loxWidgets
Some useful utils and widgets for android development

**Blur image:**  
```RenderScriptManager rs = RenderScriptManager.create(context);  
Bitmap bitmap = rs.blurImage(image);  
rs.destroy();```  
  
**Cross-fade drawable:**  
```CrossfadeDrawable crossfader = new CrossfadeDrawable(fromBitmapDrawable); // fromBitmapDrawable is optional  
getRootView().setBackground(crossfader);  
crossfader.startTransition(300, toBitmapDrawable); // can be called multiple times```  
