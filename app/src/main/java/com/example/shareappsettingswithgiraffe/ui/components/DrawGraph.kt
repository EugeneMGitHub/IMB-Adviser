package com.example.shareappsettingswithgiraffe.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun DrawGraph(
    dataList: List<Float> = listOf(0f, 25f, 0f, 50f, 0f, 100f),
    listOfDatesRaw : List<String> = listOf("13.05.2022", "13.05.2027", "13.05.2030", "13.06.2004", "13.06.2022")
){

    // the data to show
    //    val dataList = listOf( 21f, 50f, 25f, 50f, 56f, 77f, 58f, 73f, 89f)
    // list of dates for x-axis
    //    val listOfDatesRaw = listOf("13.05.2022", "13.05.2027", "13.05.2030", "13.06.2004", "13.06.2022")
    // Transform to the "dd.mm.yy" type
    val listOfDates = listOfDatesRaw.map {date ->
        val size = date.toCharArray().size
        date.removeRange(size-3,size-1)
    }

    val backGroundColor = Color(0xFF1D1D1D)
    val barColor : Color = Color(0xFF929292)

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        //BOX1
        Box(
            modifier = Modifier
                .background(backGroundColor)
                .aspectRatio(3 / 2f),
            contentAlignment = Alignment.Center
            //Alignment.TopStart,
        ){

            // For text drawing we use textMeasurer.
            // TextMeasurer is responsible for measuring a text in its entirety so that it's ready to be drawn.
            val textMeasurer = rememberTextMeasurer()

            // Before we used Canvas
            // but we want to draw some paths
            // and we don't want to be recreating a path object on every recomposition.
            // To avoid that,
            // we can switch to using the drawWithCache modifier,
            // which will take care of caching the objects
            // until the size of the drawing area changes.
            // It's worth noting that you should only use this
            // if you are creating objects in the draw functions.
            // Otherwise, it will create unnecessary lambdas.
            Spacer(modifier = Modifier
                .padding(start = 60.dp, end = 40.dp, bottom= 20.dp)
                .aspectRatio(3 / 2f)
                .fillMaxSize()
                .drawWithCache {


                    // Creates the path that can be drawn
                    // Inside GeneratePath() fun the data is also transformed( the data occupies the whole area and also rotated)
                    // with TransformTheData() fun
                    val path = GeneratePath(dataList, size)
                    // Creates path for gradient
                    val filledPath = CreatePathForGradient(path = path, size = size)

                    // Start to draw
                    // draws content behind the content of the composable
                    onDrawBehind {

                        /**Draws a box and vertical and horizontal lines */
                        /**Draws a box and vertical and horizontal lines */
                        /**Draws a box and vertical and horizontal lines */
                        /**Draws a box and vertical and horizontal lines */
                        /**Draws a box and vertical and horizontal lines */
                        /**Draws a box and vertical and horizontal lines */
                        /**Draws a box and vertical and horizontal lines */
                        /**Draws a box and vertical and horizontal lines */

                        // Draw a border for the drawing area
                        val barWidthPx = 1.dp.toPx()
                        drawRect(barColor, style = Stroke(barWidthPx))

                        // Draw vertical lines
                        val verticalLines = 3
                        val verticalSize = size.width / (verticalLines + 1)
                        repeat(verticalLines) { i ->
                            val startX = verticalSize * (i + 1)
                            drawLine(
                                barColor,
                                start = Offset(x = startX, y = 0f),
                                end = Offset(x = startX, y = size.height),
                                strokeWidth = barWidthPx
                            )
                        }

                        // Draw horizontal lines
                        val horizontalLines = 3
                        val horizontalSize = size.height / (horizontalLines + 1)
                        repeat(horizontalLines) { j ->
                            val startY = horizontalSize * (j + 1)
                            drawLine(
                                barColor,
                                start = Offset(x = 0f, y = startY),
                                end = Offset(x = size.width, y = startY),
                                strokeWidth = barWidthPx
                            )
                        }


                        /**Drawing text along the x and y axis*/
                        /**Drawing text along the x and y axis*/
                        /**Drawing text along the x and y axis*/
                        /**Drawing text along the x and y axis*/
                        /**Drawing text along the x and y axis*/
                        /**Drawing text along the x and y axis*/
                        /**Drawing text along the x and y axis*/
                        /**Drawing text along the x and y axis*/



                        // Drawing text of dates along the x-axis
                        val amountOfDates = listOfDates.size
                        val horizontalStep = size.width / (amountOfDates - 1)
                        // repeat starts from ZERO not ONE
                        repeat(amountOfDates) { i ->
                            val startX = (horizontalStep * i)
                            val measuredText =
                                textMeasurer.measure(
                                    AnnotatedString(listOfDates[i]),
                                    style = TextStyle(
                                        fontSize = 10.sp,
                                        color = Color(0xFF929292)
                                    )
                                )
                            drawText(
                                textLayoutResult = measuredText,
                                topLeft = Offset(
                                    x = startX - (measuredText.size.width / 2),
                                    y = size.height + (measuredText.size.height)
                                ),
                            )
                        }

                        // Drawing vertical values
                        val listOfValuesToDraw = mutableListOf<Float>()

                        val minValue = dataList.min()
                        val maxValue = dataList.max()
                        val differenceInValues = maxValue - minValue
                        val verticalStepForValues: Float =
                            differenceInValues / (horizontalLines + 1)

                        for (i in 0..(horizontalLines + 1)) {
                            listOfValuesToDraw.add(maxValue - (i * verticalStepForValues))
                        }
                        // repeat starts from ZERO not ONE
                        repeat(listOfValuesToDraw.size) { j ->
                            val startY = horizontalSize * (j)
                            val measuredText =
                                textMeasurer.measure(
                                    AnnotatedString(listOfValuesToDraw[j].toString()),
                                    style = TextStyle(
                                        fontSize = 10.sp,
                                        color = Color(0xFF929292)
                                    )
                                )
                            drawText(
                                textLayoutResult = measuredText,
                                topLeft = Offset(
                                    x = 0f - (measuredText.size.width * 1.3f),
                                    y = startY - 20
                                ),
                            )
                        }

                        /**Drawing the Path and the gradient under it*/

                        /**Drawing the Path and the gradient under it*/

                        /**Drawing the Path and the gradient under it*/

                        /**Drawing the Path and the gradient under it*/

                        /**Drawing the Path and the gradient under it*/

                        /**Drawing the Path and the gradient under it*/

                        /**Drawing the Path and the gradient under it*/

                        /**Drawing the Path and the gradient under it*/
                        // Drawing the path we created before

                        drawPath(
                            path = path,
                            color = Color.Green,
                            style = Stroke(2.dp.toPx())
                        )

                        //Drawing the gradient
                        val brush = Brush.verticalGradient(
                            listOf(
                                Color.Green.copy(alpha = 0.4f),
                                Color.Transparent
                            )
                        )
                        drawPath(
                            path = filledPath,
                            brush = brush,
                            style = Fill
                        )
                    }
                }
            )
        }
    }


}


fun GeneratePath(data: List<Float>, size: Size): Path {
    val path = Path()

    // transform the data so that it rotated and scaled
    val transformedData = TransformTheData(data,size)

    // matching the size of the data (the number of indexes) to the drawing area's width
    // The coefficient maps values along the entire x-axis
    val coefficientX = size.width / transformedData.lastIndex

    // move the starting point of the path so the there is no unnecessary first line
    path.moveTo(0f,transformedData[0])
    transformedData.forEachIndexed{i, value ->
        val x = coefficientX * i
        val y = value
        path.lineTo(x,y)

    }
    return path
}

fun TransformTheData(data: List<Float>,size: Size) : List<Float>{


    val heightOfDrawingArea = size.height
    Log.d("TransformTheDataFun", "heightOfDrawingArea = $heightOfDrawingArea")

    // matching the values of the data to the drawing area's height
    var coefficientY = 1f
    // Aligning the path so that the starting point of the graph aligns with the y-axis at 0
    val alignedData  = data.map { value ->
        value - data.min()
    }

    //To align the maximum point of a graph with the maximum value on the y-axis, I would need to multiply every item on the graph by a coefficient.
    // It's called a scaling factor
    val maxValue = alignedData.max()

    var scaledData1 : List<Float> = emptyList()

    if (maxValue <= heightOfDrawingArea){
        coefficientY = heightOfDrawingArea / maxValue
        scaledData1 = alignedData.map{
                value ->
            value * coefficientY
        }
    }
    else {
        coefficientY = maxValue / heightOfDrawingArea
        scaledData1 = alignedData.map{
                value ->
            value / coefficientY
        }
    }


    Log.d("TransformTheDataFun", "scaledData1 = $scaledData1")
    // rotate the Data so that it start at (0, size.height) and ends at (size.width,size.height)
    val rotatedData = scaledData1.map { value ->
        heightOfDrawingArea - value
    }
    Log.d("TransformTheDataFun", "rotatedData = $rotatedData")


    // Reverse the rotatedData list coz somehow it changes the order of the previous list
//    val listUpsideDown = rotatedData.reversed()

//    return listUpsideDown
    return rotatedData

}


fun CreatePathForGradient(path: Path, size: Size): Path {

    // filling the area below the graph with the semi-transparent gradient
    val filledPath = Path()
    filledPath.addPath(path)
    // create a closed area to fill in
    // we need to add two more lines
    // To create this closed area, aligned to the bottom right of the graph
    //and then another line to the bottom left of the graph where we started.
    filledPath.lineTo(size.width, size.height)
    filledPath.lineTo(0f,size.height)
    filledPath.close()

    return filledPath

}