REPO?=$(PWD)/sandbox
REMOTE=$(REPO)-remote.git

.PHONY: sandbox
sandbox:
	git init --bare $(REMOTE)
	git clone $(REMOTE) $(REPO)

.PHONY: clean
clean:
	rm -rf $(REMOTE)
	rm -rf $(REPO)
