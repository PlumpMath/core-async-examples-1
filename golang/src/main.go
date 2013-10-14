package main

import (
	"fmt"
	"time"
	"net/http"
)

var mychan chan string

func main() {

	mychan = make(chan string, 0)

	//close( mychan )

	go func () {
		mychan <- "Hello World\n\n"
	}()

	fmt.Printf(<- mychan)
	timers()
}

func Download(out chan string, url string) {
	go func() {
		_, error :=  http.Get(url)
		out <- error.Error()
	}()
}

func timers() {

	 site := make(chan string)

	 Download(site, "https://news.ycombinator.com")

	 var info string

	 select {
		 case info = <- site:
		 case <- time.After(0 * 1e9):
			 info = "timed out"
	 }
	 fmt.Print(info)
}

