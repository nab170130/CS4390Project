SRC_ROOT = src/com/github/project/
BIN_ROOT = target/classes/com/github/project/
CSD = $(SRC_ROOT)client/
CBD = $(BIN_ROOT)client/
COSD = $(SRC_ROOT)core/
COBD = $(BIN_ROOT)core/
SSD = $(SRC_ROOT)server/
SBD = $(BIN_ROOT)server/

CORE_CLASSES = ResponseCode Response Request CalculationRequest CalculationResponse ConnectionEstablishRequest ConnectionEstablishResponse ConnectionTerminateRequest ConnectionTerminateResponse
CLIENT_CLASSES = MathClientMain
SERVER_CLASSES = ConnectionHandler Logger MathServerMain ProcessingQueue QueueElement

CORE_BIN = $(addprefix $(COBD), $(addsuffix .class,$(CORE_CLASSES)))
CLIENT_BIN = $(addprefix $(CBD), $(addsuffix .class,$(CLIENT_CLASSES)))
SERVER_BIN = $(addprefix $(SBD), $(addsuffix .class,$(SERVER_CLASSES)))

CORE_SRC = $(addprefix $(COSD), $(addsuffix .java,$(CORE_CLASSES)))
CLIENT_SRC = $(addprefix $(CSD), $(addsuffix .java,$(CLIENT_CLASSES)))
SERVER_SRC = $(addprefix $(SSD), $(addsuffix .java,$(SERVER_CLASSES)))

.PHONY: all clean

all: client.jar server.jar

clean:
	rm -rf *~
	rm -f client.jar
	rm -f server.jar

client.jar: $(CORE_BIN) $(CLIENT_BIN) $(CBD)manifest.mf
	jar -cvfm client.jar $(CBD)manifest.mf -C target/classes com/github/project/core -C target/classes com/github/project/client

server.jar: $(SERVER_BIN) $(CORE_BIN) $(SBD)manifest.mf
	jar -cvfm server.jar $(SBD)manifest.mf -C target/classes com/github/project/core -C target/classes com/github/project/server -C target/classes org

$(CORE_BIN): $(COBD)%.class : $(COSD)%.java
	javac $< -cp src/ -d target/classes

$(CLIENT_BIN): $(CBD)%.class : $(CSD)%.java
	javac $(CLIENT_SRC) -cp src/ -d target/classes

$(SERVER_BIN): $(SBD)%.class : $(SSD)%.java
	javac $(SERVER_SRC) -cp ".:src/:target/classes/" -d target/classes
