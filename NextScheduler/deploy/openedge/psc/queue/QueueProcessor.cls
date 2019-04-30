 
/*------------------------------------------------------------------------
   File        : QueueProcessor
   Purpose     : 
   Syntax      : 
   Description : 
   Author(s)   : 
   Created     : 
   Notes       : 
 ----------------------------------------------------------------------*/

block-level on error undo, throw.

using OpenEdge.Logging.ILogWriter from propath.
using OpenEdge.Logging.LoggerBuilder from propath.

class psc.queue.QueueProcessor:
     
    define public property logger as ILogWriter no-undo
        get():
            if not valid-object(logger) then
                assign logger = LoggerBuilder:GetLogger(this-object:GetClass()).
            return logger.               
        end.
        set.

    /* must have a zero-parameter constructor */
    constructor public QueueProcessor ():
        logger:debug('New QueueProcessor instance created').
    end.
    
    // TODO: should use "-rand 2" startup parameter 
    method public integer runAllScheduled():
        define variable retcount as integer no-undo.
        
        assign retcount = random(1,5000).
        
        logger:Debug(subst('Processed all requests: &1',retcount)).
        
        return retcount.
    end.    
end class.