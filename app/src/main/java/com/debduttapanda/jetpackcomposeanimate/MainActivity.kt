package com.debduttapanda.jetpackcomposeanimate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Spring.DampingRatioHighBouncy
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.debduttapanda.jetpackcomposeanimate.ui.theme.JetpackComposeAnimateTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
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
                    var job by remember { mutableStateOf<Job?>(null) }
                    val minWidth = 300f
                    val maxWidth = 1150f
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
                               //.width((width.value/density.density).dp)
                               .fillMaxWidth()
                               .clickable {
                                   growing = !growing
                                   job?.cancel()//cancel the ongoing if any
                                   job = scope.launch {
                                       width.animateTo(
                                           if (growing) maxWidth else minWidth,
                                           spring(
                                               dampingRatio = DampingRatioHighBouncy,
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
                               val count = 15
                               val dx = width.value/count.toFloat()
                               val stroke = 4f
                               repeat(count){
                                   drawArc(
                                       color = Color.Blue,
                                       topLeft = Offset(x+it*(dx*2f/3f),0f),
                                       size = Size(dx,height.toFloat()),
                                       style = Stroke(
                                           stroke
                                       ),
                                       startAngle = 0f,
                                       sweepAngle = when(it){
                                                            0->-90f
                                           else->-180f
                                                            },
                                       useCenter = false
                                   )
                                   if(it<count){
                                       drawArc(
                                           color = Color.Blue,
                                           topLeft = Offset(x+(2*dx/3f)*(it+1),0f),
                                           size = Size(dx/3f,height.toFloat()),
                                           style = Stroke(
                                               stroke
                                           ),
                                           startAngle = 0f,
                                           sweepAngle = if(it==count-1) 90f else 180f,
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