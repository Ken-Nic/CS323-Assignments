#include <iostream>
#include<fstream>
#include<string>
using namespace std;

class listNode
{
    private:
    int data;
    listNode* nextNode;
    friend class LLStack;
    friend class LLQueue;

    public:
    // Constructor
    listNode(int d){
        this->data = d;
        this->nextNode = NULL;
    }
 
    // Getters
    int getData(){
        return this->data;
    }

    listNode* getNext(){
        return this->nextNode;
    }

    // Setters
    void setData(int n){
        this->data = n;
    }

    void setNext(listNode * n){
        this->nextNode = n;
    }

    string printNode(){ 
        string output;
        (this->nextNode != NULL)  ? output = "(" + to_string(this->data) + "," + to_string(this->nextNode->getData()) + ")" : output = "(" + to_string(this->data) + "," + "NULL)";
        return output;
    }

    friend class LLStack;
    friend class LLQueue;
    friend class LLlist;
};

class LLStack{
    private:
    listNode * topNode;

    public:

    LLStack(){
       this->topNode = new listNode(-9999);
    }

    void buildStack(ifstream & inFile, ofstream & outFile){
        char sign;
        int num;
        while(inFile >> sign >> num){
            if(sign == '+') {
                listNode * newNode = new listNode(num);
                push(newNode);

            }
            else if(sign == '-'){
                listNode*junk = this->pop();
                if(junk != NULL) {
                    delete junk;
                }
            } 
            printStack(outFile);
        }      
    }

    void push(listNode * n){
        n->setNext(this->topNode->getNext());
        this->topNode->setNext(n);
    }

    listNode * pop(){
        if(!(this->isEmpty())){

            listNode * ret;
            ret = this->topNode->getNext();
            this->topNode->setNext(ret->getNext());
            return ret;
        }

        return NULL;
    }

    bool isEmpty(){
        if(this->topNode->getNext() == NULL){
            return true;
        }
        return false;
    }

     void printStack(ofstream& outFile){
        listNode * runner = this->topNode;
        while(runner != NULL){
            outFile<<runner->printNode()<<"-->";
            runner = runner->nextNode;
        }
        outFile << "NULL\n\n";
    }


};

class LLQueue{
    private:
    listNode * head;
    listNode * tail;

    public:
    LLQueue(){
        this->head = new listNode(-9999);
        this->tail = this->head;
    }

    void insertQ(listNode * n){
        this->tail->setNext(n);
        this->tail = n;
    }

    listNode* deleteQ(){
        if(!(isEmpty())){
            listNode * ret = this->head->nextNode;
            if(this->tail == ret){
                this->tail = this->head;
            }
            this->head->nextNode = ret->nextNode;
            return ret;
        }
        return NULL;
    }

    bool isEmpty(){
        if(this->head == this->tail){
            return true;
        }
        return false;
    }

    void buildQueue(ifstream & infile, ofstream & outfile){
        char sign;
        int num;
        while(infile >> sign >> num){
            if(sign == '+') {
                listNode * newNode = new listNode(num);
                insertQ(newNode);

            }
            else if(sign == '-'){
                listNode * junk = deleteQ();

                if(junk != NULL)
                {
                    delete junk;
                }

                
            }  
            printQ(outfile);
        }
    }

    void printQ(ofstream& outFile){
        listNode * runner = this->head;
        while(runner != NULL){
            outFile<<runner->printNode()<<"-->";
            runner = runner->nextNode;
        }
        outFile << "NULL\n\n";
    }
};

class LLlist{
    private:
    listNode * listhead;

    public:

    LLlist () 
    {
        this->listhead = new listNode(-9999);
        this->listhead->nextNode = nullptr;
    }

    void listInsert (int newData)
    {
        listNode * runner = this->listhead;
        while(runner->nextNode != NULL && runner->nextNode->data <= newData)
        {
            runner = runner->nextNode;
        }
            listNode * newNode = new listNode(newData);
            newNode->nextNode = runner->nextNode;
            runner->nextNode = newNode;
    }

    void deleteOneNode (int target, ofstream & outfile)
    {
        listNode * runner = this->listhead;

        while(runner->nextNode != NULL && runner->nextNode->data < target)
        {
            runner = runner->nextNode;
        }

        if(runner->nextNode != NULL && runner->nextNode->data == target)
        {
            listNode * save = runner->nextNode;
            runner->nextNode = save->nextNode;
            delete save;
        }
        else
        { 
            outfile <<"|\nValue: " << target << " was not on the list, as such it was not removed\n|\n\n";
        }


    }

    void buildList (ifstream & infile, ofstream & outfile)
    {
        char sign = ' ';
        int num = 0;
        while(infile>>sign>>num)
        {
            if(sign == '+')
            {
                this->listInsert(num);
            }
            else
            {
                this->deleteOneNode(num,outfile);
            }
            this->printList(outfile);
        }
    }

    void printList (ofstream & outfile)
    {
        outfile<<"listHead-->";
        listNode * runner = this->listhead;
        while(runner != NULL)
        {
            outfile << runner->printNode()<<"-->";
            runner = runner->nextNode;
        }
        outfile<<"NULL\n\n";
    }
    
};;

int main(int argc, const char * argv[]) 
{
    // Setting up input and output files
    ifstream in;
    ofstream queueOutput;
    ofstream stackOutput;
    ofstream listOutput;

    // Stack
    LLStack* stack = new LLStack();
    // Queue
    LLQueue* queue = new LLQueue();
    // List
    LLlist* list = new LLlist();

    // Stack output
    in.open(argv[1]);
    stackOutput.open(argv[2]);
    stackOutput<< "----------LLStack Output----------\n";
    stack->buildStack(in,stackOutput);
    in.close();
    stackOutput.close();

    // Queue output
    in.open(argv[1]);
    queueOutput.open(argv[3]);
    queueOutput << "----------LLQueue Output----------\n";
    queue->buildQueue(in,queueOutput);
    in.close();
    queueOutput.close();

    // List output
    in.open(argv[1]);
    listOutput.open(argv[4]);
    listOutput << "----------LLlist Output----------\n";
    list->buildList(in,listOutput);
    in.close();
    listOutput.close();
}