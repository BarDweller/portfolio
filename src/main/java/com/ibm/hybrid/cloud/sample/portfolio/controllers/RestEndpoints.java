/*
       Copyright 2018 IBM Corp All Rights Reserved
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
       http://www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.ibm.hybrid.cloud.sample.portfolio.controllers;

import java.util.List;

import com.ibm.hybrid.cloud.sample.portfolio.controllers.datamodel.Feedback;
import com.ibm.hybrid.cloud.sample.portfolio.controllers.datamodel.FeedbackReply;
import com.ibm.hybrid.cloud.sample.portfolio.controllers.datamodel.Portfolio;
import com.ibm.hybrid.cloud.sample.portfolio.service.OwnerNotFoundException;
import com.ibm.hybrid.cloud.sample.portfolio.service.PortfolioService;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestEndpoints {

    @Autowired
    PortfolioService service;

    ModelMapper mapper = new ModelMapper();

    @GetMapping("/")
    public List<Portfolio> getAllPortfolios() { 
        return mapper.map(service.getAllPortfolios(), new TypeToken<List<Portfolio>>() {}.getType());
    }

    @PostMapping("/{owner}")
    public Portfolio createPortfolio(@PathVariable String owner){
        return mapper.map(service.createNewPortfolio(owner), Portfolio.class);
    }

    @GetMapping("/{owner}")
    public Portfolio getPortfolio(@PathVariable String owner) throws OwnerNotFoundException{
        return  mapper.map(service.getPortfolio(owner), Portfolio.class);
    }

    @PutMapping("/{owner}")
    public Portfolio updatePortfolio(@PathVariable String owner,
                                    @RequestParam("symbol") String symbol, 
                                    @RequestParam("shares") int shares ) throws OwnerNotFoundException{
        return  mapper.map(service.updatePortfolio(owner,symbol,shares), Portfolio.class);
    }

    @DeleteMapping("/{owner}")
    public Portfolio deletePortfolio(@PathVariable String owner) throws OwnerNotFoundException{
        return mapper.map(service.deletePortfolio(owner), Portfolio.class);
    }  
    
    @PostMapping("/{owner}/feedback")
    public FeedbackReply submitFeedback(@PathVariable String owner, Feedback feedback){
        //xTODO: this won't work.. submitFeedback will need to return something else.
        return mapper.map(service.submitFeedback(owner,feedback.getText()), FeedbackReply.class);
    }    

    @ExceptionHandler( {OwnerNotFoundException.class} )
    public ResponseEntity<String> handleNotFound() {
        return ResponseEntity.notFound().build();
    }
    

}
