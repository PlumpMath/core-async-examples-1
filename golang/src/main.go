package main

import "fmt"

var mychan chan string

func main() {

	mychan = make(chan string, 0)

	go func () {
		mychan <- "Hello World"
	}()

	fmt.Printf(<- mychan)
}
