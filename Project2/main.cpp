#include <iostream>
#include<fstream>
#include<string>
using namespace std;

class listNode{
    private:
    char op;
    string firstName;
    string lastName;
    listNode * next;

    public:
    listNode()
    {
        this->firstName = "dummyFirstName";
        this->lastName = "dummyLastName";
        this->next = NULL;
    }

    listNode(string f,string l)
    {
        this->firstName = f;
        this->lastName = l;
        this->next = NULL;
    }

    string printNode(){ 
        string output;
        (this->next != NULL)  ? output = "(" + this->firstName + " " + this->lastName + " " + this->next->firstName + ")" : output = "(" + this->firstName + " " + this->lastName + " NULL)";
        return output;
    }

    friend class hashTable;
};

class hashTable
{
    private:
    int bucketSize;
    listNode ** hash;

    public:
    hashTable(int size)
    {
        this->bucketSize = size;
    }
    
    void createHashTable()
    {
        this->hash = new listNode * [this->bucketSize];
        for(int i = 0; i < bucketSize; i++) {
            this->hash[i] = new listNode();
        }
    }

    // HashFunction
    int Doit(string lastName, int hashTableSize)
    {
        unsigned int val = 1;
        for(int i = 0; i<lastName.length(); i++)
        {
            val = val * 32 + (int)lastName[i];
        }

        return (val % hashTableSize);
    }

    listNode * findSpot (int index, string firstName, string lastName)
    {
        listNode * spot = hash[index];

        while(spot->next != NULL && spot->next->lastName < lastName)
        {
            spot = spot->next;
        }
        while(spot->next != NULL && spot->next->lastName == lastName && spot->next->firstName < firstName)
        {
            spot = spot->next;
        }
        return spot;
    }

    //Hash List methods (Insert, Delete, Retrieve)
    void hashInsert(int index, string firstName, string lastName, ofstream & output)
    {
        output << "performing hashInsert on " << firstName <<" "<< lastName << endl;
        listNode * spot = findSpot(index,firstName,lastName);
        if(spot->next != NULL && spot->next->lastName == lastName && spot->next->firstName == firstName)
        {
           output << "Warning, the record of " << firstName << " " << lastName <<" is already in the database\n";
        }
        else
        {
            listNode * newNode = new listNode(firstName,lastName);
            newNode->next = spot->next;
            spot->next = newNode;
            printList(index,output);
        }
    }

    void hashDelete(int index, string firstName, string lastName, ofstream & output)
    {
        output << "Performing hashDelete on " << firstName << " " << lastName << endl;
        listNode * spot = findSpot(index,firstName,lastName);

        if(spot->next != NULL && spot->next->lastName == lastName && spot->next->firstName == firstName)
        {
            listNode * junk = spot->next;
            spot->next = junk->next;
            junk->next = NULL;
            delete(junk);
            printList(index,output);
        } 
        else
        {
            output<<"No, the record of " <<firstName << " " << lastName<<" is not in the database\n";
        }
    }

    void hashRetrieval(int index, string firstName, string lastName, ofstream & output)
    {
        output<<"Performing hashRetrieval on " <<firstName<<" "<<lastName<<endl;
        listNode * spot = findSpot(index,firstName,lastName);
        if(spot->next !=NULL && spot->next->lastName == lastName && spot->next->firstName == firstName)
        {
            output<<"Yes, the record of " <<firstName << " " << lastName<<" is in the database\n";
        } 
        else
        {
            output<<"No, the record of " <<firstName << " " << lastName<<" is not in the database\n";
        }
    }


    void informationProcessing(ifstream & input, ofstream & output)
    {
        char op;
        string firstName;
        string lastName;
        int index;

        while(input>>op>>firstName>>lastName)
        {
            output<<"Got: "<<op<<" "<<firstName<<" "<<lastName<<"\n";
            
            index = Doit(lastName,this->bucketSize);
            output<<"index: "<<index<<"\n";

            printList(index,output);

            if(op == '+')
            {
                hashInsert(index,firstName,lastName,output);
                output<<"\n";

            } else if (op == '-') 
            {
                hashDelete(index,firstName,lastName,output);
                output<<"\n";

            } else if (op == '?')
            {
                hashRetrieval(index,firstName,lastName,output);
                output<<"\n";
            }
        }
    }

    // Print methods
    void printList(int index, ofstream & output)
    {
        output<<"HashTable["<<index<<"]: ";
        listNode * runner = this->hash[index];

        while(runner != NULL) {
            output << runner->printNode() << "-->";
            runner = runner->next;
        }
        output<<"NULL\n";
    }

    void printHashTable(ofstream & output)
    {
        for(int i = 0; i < bucketSize;i++)
        {
            printList(i,output);
        }
    }
};

int main(int argc, const char * argv[])
{
    // Setting up inputs from CLI
    ifstream input;
    ofstream output1, output2;

    input.open(argv[1]);
    int bucketSize = atoi(argv[2]);
    output1.open(argv[3]);
    output2.open(argv[4]);
  
    hashTable * h = new hashTable(bucketSize);
    h->createHashTable();
    h->informationProcessing(input,output2);
    h->printHashTable(output1);

    // Closing all files
    input.close();
    output1.close();
    output2.close();
} 