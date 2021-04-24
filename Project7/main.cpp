#include<string>
#include<iostream>
#include<fstream>
using namespace std;


class uEdge
{
    public:
    int Ni,Nj,cost;
    uEdge * next;

    //Methods

    uEdge(int ni, int nj, int c, uEdge * n)
    {
        Ni = ni;
        Nj = nj;
        cost = c;
        next = n;
    }

    void printEdge(ofstream & out)
    {
        out << "<" << Ni << "," << Nj << "," << cost;
        if(next!=NULL)
        {
            out <<","<<next->Ni;
        }
        out <<">";
    }
};

class PrimMST
{
    int numNodes,nodeInSetA;
    int * whichSet;
    uEdge * edgelistHead;
    uEdge * MSTlistHead;
    int totalMSTCost = 0;

    // Methods
    public:
    PrimMST(int n,int s)
    {
        numNodes = n;
        nodeInSetA = s;
        whichSet = new int[numNodes + 1]{0};
        for(int i = 0; i < numNodes; i++)
        {
            whichSet[i] = 2;
        }
        edgelistHead = new uEdge(0,0,0,NULL);
        MSTlistHead = new uEdge(0,0,0,NULL);
    }

    // Getters & Setters
    int * getSet()
    {
        return whichSet;
    }

    int getTotalMSTCost ()
    {
        return totalMSTCost;
    }
    void listInsert(uEdge * n)
    {
        uEdge * runner = edgelistHead;
        while(runner->next != NULL && runner->next-> cost < n->cost)
        {
            runner = runner->next;
        }

        n->next = runner->next;
        runner->next = n;
    }


    void addEdge(uEdge * n)
    {
        n->next = MSTlistHead->next;
        MSTlistHead->next = n;
    }

    uEdge * removeEdge()
    {
        uEdge * runner = edgelistHead;
        uEdge * e;
        while(runner->next != NULL)
        {
            if(whichSet[runner->next->Ni] != whichSet[runner->next->Nj] && (whichSet[runner->next->Ni] == 1 || whichSet[runner->next->Nj] == 1))
            {
                e = runner->next;
                runner->next = e->next;
                return e; 
            }
            runner = runner->next;
        }
        return NULL;
    }

    void printSet(ofstream & out)
    {
        for(int i = 0; i < numNodes; i++)
        {
            out<<whichSet[i]<<" ";
        }
        out << "\n";
    }

    void printEdgeList(ofstream & out)
    {
        uEdge * runner = edgelistHead;
        while(runner != NULL)
        {
            runner->printEdge(out);
            out<<"->";
            runner = runner->next;
        }
    }

    void printMSTList(ofstream & out)
    {
        uEdge * runner = MSTlistHead->next;
        while(runner != NULL)
        {
            runner->printEdge(out);
            out<<"->";
            runner = runner->next;
        }
    }

    bool setBisEmpty()
    {
        for(int i = 1; i < numNodes; i++)
        {
            if(whichSet[i] != 1)
            {
                return false;
            }
        }
        return true;
    }

    void updateMST(uEdge * n)
    {
        addEdge(n);
        totalMSTCost += n->cost;

        if(whichSet[n->Ni] == 1)
        {
            whichSet[n->Nj] = 1;
        }
        else
        {
            whichSet[n->Ni] = 1;
        }
    }


};

int main (int argc, const char * argv[])
{

    // Step 0
    ofstream debug,MSTfile;
    ifstream infile;
    infile.open(argv[1]);
    debug.open(argv[4]);
    MSTfile.open(argv[3]);
    int numNodes;
    infile >> numNodes;
    int nodeInSetA = atoi(argv[2]);
    PrimMST * tree = new PrimMST(numNodes,nodeInSetA);
    tree->getSet()[nodeInSetA] = 1;
    tree->printSet(debug);
    
    // Step 3
    int Ni,Nj,Cost;
    while(infile >> Ni >> Nj >> Cost)
    {
        // Step 1
        uEdge * newEdge = new uEdge(Ni,Nj,Cost,NULL);
        tree->listInsert(newEdge);

        // Step 2
        tree->printEdgeList(debug);
        debug <<"NULL\n--------------------------------------\n";
    }


    
    // // Step 9
    while(!(tree->setBisEmpty()))
    {
    // Step 4
    uEdge * nextEdge = tree->removeEdge();

    // Step 5
    nextEdge->printEdge(debug);
    debug<<"\n";

    // Step 6
    tree->updateMST(nextEdge);

    // Step 7
    tree->printSet(debug);

    // Step 8
    tree->printEdgeList(debug);
    debug<<"NULL\n";
    tree->printMSTList(debug);
    debug<<"NULL\n\n\n\n\n";
    }


    // Step 10
    MSTfile << "*** Prim's MST of the input graph, G is:***\n"<<"NumNodes: "<<numNodes << "\n";
    tree->printMSTList(MSTfile);
    MSTfile<<"NULL";
    MSTfile << "\n ***MST total cost= " <<tree->getTotalMSTCost();


    // Step 11
    infile.close();
    MSTfile.close();
    debug.close();
}