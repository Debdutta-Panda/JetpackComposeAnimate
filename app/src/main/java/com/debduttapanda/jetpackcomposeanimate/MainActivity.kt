package com.debduttapanda.jetpackcomposeanimate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Spring.StiffnessHigh
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.debduttapanda.jetpackcomposeanimate.ui.theme.JetpackComposeAnimateTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeAnimateTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val minWidth = 32f
                    val maxWidth = 200f
                    val width = remember { Animatable(minWidth) }
                    val scope = rememberCoroutineScope()
                    var growing by remember { mutableStateOf(false) }
                    var size by remember { mutableStateOf(IntSize.Zero) }
                    Box(
                        contentAlignment = Alignment.CenterStart
                    ){
                       Box(
                           modifier = Modifier
                               .padding(12.dp)
                               .height(64.dp)
                               .width(width.value.dp)
                               /*.clip(
                                   shape = RoundedCornerShape(8.dp)
                               )
                               .background(
                                   color = Color.Blue
                               )*/
                               .clickable {
                                   growing = !growing
                                   scope.launch {
                                       width.animateTo(
                                           if (growing) maxWidth else minWidth,
                                           spring(
                                               dampingRatio = 0.35f,
                                               stiffness = StiffnessLow

                                           )
                                       )
                                   }
                               }
                               .onGloballyPositioned {
                                   size = it.size
                               }
                       ){
                           Canvas(
                               modifier = Modifier.fillMaxSize()
                           ){
                               var x = 0f
                               var height = size.height
                               val count = 10
                               val dx = size.width.toFloat()/count.toFloat()
                               repeat(count){
                                   val y1 = if(it%2==1) height.toFloat() else 0f
                                   val y2 = height - y1
                                   drawArc(
                                       color = Color.Blue,
                                       topLeft = Offset(x+it*(dx*2f/3f),0f),
                                       size = Size(dx,height.toFloat()),
                                       style = Stroke(
                                           2f
                                       ),
                                       startAngle = 0f,
                                       sweepAngle = when(it){
                                                            0->-90f
                                           else->-180f
                                                            },
                                       useCenter = false
                                   )
                                   if(it<count-1){
                                       drawArc(
                                           color = Color.Blue,
                                           topLeft = Offset(x+(2*dx/3f)*(it+1),0f),
                                           size = Size(dx/3f,height.toFloat()),
                                           style = Stroke(
                                               2f
                                           ),
                                           startAngle = 0f,
                                           sweepAngle = 180f,
                                           useCenter = false
                                       )
                                   }
                               }
                           }
                       }
                    }
                }
            }
        }
    }
}