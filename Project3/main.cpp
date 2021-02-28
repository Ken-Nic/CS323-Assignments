#include<iostream>
#include<fstream>
#include<string>
using namespace std;

class listNode
{
    private:
    int data;
    listNode* nextNode;

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
    friend class RadixSort;
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
            ret->nextNode = NULL;
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

    void printQueue(ofstream & outfile)
    {
        listNode * runner = this->head;
        while(runner != NULL)
        {
            outfile << runner->printNode() <<"-->";
            runner = runner->nextNode;
        }
        outfile <<"NULL\n";       
    }

     void printQueueOffset(ofstream & outfile,int offset)
    {
        listNode * runner = this->head->nextNode;
        while(runner != NULL)
        {
            outfile << runner->data - offset << endl;
            runner = runner->nextNode;
        }      
    }
    friend class RadixSort;
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
            ret->setNext(NULL);
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

class RadixSort
{
    private:
    int tableSize = 10;
    int data;
    int currentTable;
    int previousTable;
    int numDigits;
    int offSet;
    int currentPosition;
    int currentDigit;
    LLQueue * hashTable[2][10];

    public:
    RadixSort()
    {
        for(int i = 0; i < 2; i++)
        {
            for(int j = 0; j < this->tableSize; j++)
            {
                this->hashTable[i][j] = new LLQueue();
            }
        }
    }

    // Methods
    int getLength(int data)
    {
        string l = to_string(data);
        return l.length();
    }

    void firstReading(ifstream & infile, ofstream & outfile)
    {
        outfile << "***Performing firstReading\n";
        int negativeNum = 0, positiveNum = 0,data;

        while(infile >> data)
        {
            if(data < negativeNum)
            {
                negativeNum = data;
            }

            if(data > positiveNum)
            {
                positiveNum = data;
            }
        }

        if(negativeNum < 0)
        {
            this->offSet = abs(negativeNum);
        }
        else
        {
            this->offSet = 0;
        }

        positiveNum += this->offSet;
        this->numDigits = this->getLength(positiveNum);
        outfile << "PositveNum: " <<positiveNum<<"\nNegativeNum: "<<negativeNum<<"\noffSet: "<<this->offSet<<"\nnumDigits: "<<this->numDigits<<endl;
    }

    LLStack * loadStack(ifstream & infile, ofstream & output)
    {
        LLStack * S = new LLStack();
        int data;
        while(infile >> data)
        {
            data += this->offSet;
            listNode * newNode = new listNode(data);
            S->push(newNode);
        }
        return S;
    }

    int getDigit(int data, int currentPosition)
    {
        string l = to_string(data);
        while(l.length() < this->numDigits)
        {
            l = '0' + l;
        }
    
        char out = l[(l.length()-1) - currentPosition];
        int output = (int)out - (int)'0';
        return output;
    }

    void moveStack(LLStack * S, int currentPosition, int currentTable,ofstream & outfile)
    {
        outfile << "***Performing moveStack\n";
        int hashIndex;
        while(!(S->isEmpty()))
        {
        listNode * newNode = S->pop();
        hashIndex = this->getDigit(newNode->data,this->currentPosition);
        hashTable[currentTable][hashIndex]->insertQ(newNode);
        }
    }

    void printTable(int table, ofstream & outfile)
    {
       outfile<<"***Printing table\n";
       for(int i = 0; i < tableSize; i++)
       {
        if(!(hashTable[table][i]->isEmpty()))
        {
            outfile << "Table[" << table <<"]["<<i<<"]: ";
            hashTable[table][i]->printQueue(outfile);
        }
      }
      outfile << endl;
    }

    void printSortedData(int table,ofstream & outfile)
    {
        for(int i = 0; i < tableSize; i++)
        {
            hashTable[table][i]->printQueueOffset(outfile,this->offSet);
        }
    }

    void RSort(LLStack * S, ofstream & outfile, ofstream & outfile1)
    {

        outfile << "***Performing RSort\n";
        this->currentPosition = 0;
        this->currentTable = 0;
        this->moveStack(S,currentPosition,currentTable,outfile);
        this->printTable(currentTable,outfile);
        this->currentTable = 1;
        this->previousTable = 0;
        int currentQueue = 0;
        while(currentPosition < numDigits){
            while(currentQueue < this->tableSize)
            {

                while(!(hashTable[previousTable][currentQueue]->isEmpty()))
                {
                listNode * newNode = hashTable[previousTable][currentQueue]->deleteQ();
                int hashIndex = getDigit(newNode->data,this->currentPosition);
                hashTable[currentTable][hashIndex]->insertQ(newNode);
                }
                currentQueue++;
            }

            printTable(currentTable,outfile);
            previousTable = currentTable;
            currentTable = (currentTable+1) % 2;
            currentQueue = 0;
            currentPosition++;
        }
        printSortedData(previousTable,outfile1);
    };

};


int main (int argc, const char * argv[])
{
    ifstream input;
    ofstream output1,output2;

    // Execute
    input.open(argv[1]);
    output1.open(argv[2]);
    output2.open(argv[3]);

    RadixSort * radix = new RadixSort();
    LLStack * stack = new LLStack();
    radix->firstReading(input,output2);
    input.close();
    input.open(argv[1]);
    stack = radix->loadStack(input,output2);
    stack->printStack(output2);
    radix->RSort(stack,output2,output1);

    // Closing
    input.close();
    output1.close();
    output2.close();
}