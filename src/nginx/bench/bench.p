# output as png image
set terminal png

# save file to "benchmark.png"
set output "benchmark.png"

#nicer aspect ratio for image size
set size 0.95,1

# y-axis grid
set grid y

#x-axis label
set xlabel "request"

#y-axis label
set ylabel "response time (ms)"

plot "out.data" using 9 smooth sbezier with lines title "KApp"