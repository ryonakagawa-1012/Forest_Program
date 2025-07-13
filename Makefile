PYTHON	= python
PYDOC	= pydoc
PYCS	= $(shell find . -name "*.pyc")
PYCACHE	= $(shell find . -name "__pycache__")
BASE	= Example
EXT	= py
TARGET	= $(BASE).$(EXT)
MODULE	= HelloWorld.$(EXT)
PACKAGE	= jp.ac.kyoto_su.cse.ap.python
PKGPATH	= $(shell echo $(PACKAGE) | sed -e 's/\./\//g')
PKGTDIR	= $(shell echo $(PACKAGE) | cut -d '.' -f1)
INSTDIR	= Example.app/Contents/Resources/Python/
ARCHIVE	= $(shell basename `pwd`)
WORKDIR	= ./
PYLINT	= pylint
LINTRCF	= pylintrc.txt
LINTRST	= pylintresult.txt
EGG-INFO = $(shell find . -name "*.egg-info")

all:
	@:

clean:
	@for each in ${PYCS} ; do echo "rm -f $${each}" ; rm -f $${each} ; done
	@for each in ${PYCACHE} ; do echo "rm -f $${each}" ; rm -rf $${each} ; done
	@if [ -e $(INSTDIR) ] ; then echo "rm -f -r $(INSTDIR)" ; rm -f -r $(INSTDIR) ; fi
	@if [ -e $(LINTRST) ] ; then echo "rm -f $(LINTRST)" ; rm -f $(LINTRST) ; fi
	@if [ -e MANIFEST ] ; then echo "rm -f MANIFEST" ; rm -f MANIFEST ; fi
	@for each in ${EGG-INFO} ; do echo "rm -f -r $${each}" ; rm -f -r $${each} ; done
	@if [ -e setup.cfg ] ; then echo "rm -f setup.cfg" ; rm -f setup.cfg ; fi
	@find . -name ".DS_Store" -exec rm {} ";" -exec echo rm -f {} ";"
	@xattr -cr ./

wipe: clean
	(cd ../ ; rm -f ./$(ARCHIVE).zip)

test: all
	$(PYTHON) $(TARGET)

install: all
	@if [ ! -e $(INSTDIR) ] ; then echo "mkdir $(INSTDIR)" ; mkdir $(INSTDIR) ; fi
	cp -p -r $(TARGET) $(PKGTDIR) $(INSTDIR)

doc:
	$(PYDOC) ./$(TARGET) ./$(PKGPATH)/$(MODULE)

zip: wipe
	(cd ../ ; zip -r ./$(ARCHIVE).zip ./$(ARCHIVE)/ --exclude='*/.svn/*')

sdist: clean
	$(PYTHON) setup.py sdist

pydoc:
	(sleep 3 ; open http://localhost:9999/$(PACKAGE).html) & $(PYDOC) -p 9999

unittest:
	@find $(PKGTDIR) -name "[A-Za-z]*.py" -exec echo '*** ['{}'] ***' ";" -exec $(PYTHON) {} -v ";"

lint: pylint clean
	@if [ ! -e $(LINTRCF) ] ; then $(PYLINT) --generate-rcfile > $(LINTRCF) 2> /dev/null ; fi
	$(PYLINT) --rcfile=$(LINTRCF) --reports=n `find . -name "*.py"` > $(LINTRST) ; less $(LINTRST)

# 
# pip is the PyPA recommended tool for installing Python packages.
# 
pip:
	@if [ -z `which pip` ]; \
	then \
		(cd $(WORKDIR); curl -O https://bootstrap.pypa.io/get-pip.py); \
		(cd $(WORKDIR); sudo -H python get-pip.py); \
		(cd $(WORKDIR); rm -r get-pip.py); \
	else \
		(cd $(WORKDIR); sudo -H pip install -U pip); \
	fi

# 
# Pylint is a tool that checks for errors in Python code,
# tries to enforce a coding standard and looks for code smells.
# 
pylint:
	@if [ -z `pip list --format=freeze | grep pylint` ]; \
	then \
		(cd $(WORKDIR); sudo -H pip install pylint); \
	fi

# 
# List of the required packages
# 
list: pip
	@(pip list --format=freeze | grep pip)
	@(pip list --format=freeze | grep pylint)

prepare: pip pylint

update: pip pylint
