package br.com.gft.forum.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.gft.forum.controller.dto.DetalhesTopicoDto;
import br.com.gft.forum.controller.dto.TopicoDto;
import br.com.gft.forum.controller.form.TopicoForm;
import br.com.gft.forum.controller.form.UpdateTopicoForm;
import br.com.gft.forum.model.Topico;
import br.com.gft.forum.repository.CursoRepository;
import br.com.gft.forum.repository.TopicoRepository;

@RestController
@RequestMapping(value = "/topicos")
public class TopicoController {

	@Autowired
	private TopicoRepository tr;
	
	@Autowired
	private CursoRepository cr;
	
	@GetMapping
	public ResponseEntity<List<TopicoDto>> listaTopicos(String nomeCurso) {
		if (nomeCurso == null) {
		return ResponseEntity.ok().body(TopicoDto.converter(tr.findAll()));
		} else {
			return ResponseEntity.ok().body(TopicoDto.converter(tr.findByCursoNomeContaining(nomeCurso)));
		}
	}
	
	@PostMapping
	public ResponseEntity<TopicoDto> salvar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		Topico topico = form.converter(cr);
		tr.save(topico);
		return ResponseEntity.created(uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri()).body(new TopicoDto(topico));
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		tr.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<DetalhesTopicoDto> consultarTopico(@PathVariable Long id){
		return ResponseEntity.ok().body(new DetalhesTopicoDto(tr.findById(id).get()));

	}
	
	@PutMapping(value="/{id}")
	@Transactional
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid UpdateTopicoForm form){
		form.atualizar(id, tr);
		return ResponseEntity.noContent().build();		
	}
		
	
}
