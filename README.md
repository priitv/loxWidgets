# loxWidgets
Some useful utils and widgets for android development

Blur image:<br>
RenderScriptManager rs = RenderScriptManager.create(context);<br>
Bitmap bitmap = rs.blurImage(image);<br>
rs.destroy();<br>

Cross-fade drawable:<br>
CrossfadeDrawable crossfader = new CrossfadeDrawable(fromBitmapDrawable); // fromBitmapDrawable is optional<br>
getRootView().setBackground(crossfader);<br>
crossfader.startTransition(300, toBitmapDrawable); // can be called multiple times<br>
