import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {ReligiousBelief} from "./religious-belief.model";
import {ReligiousBeliefPopupService} from "./religious-belief-popup.service";
import {ReligiousBeliefService} from "./religious-belief.service";

@Component({
	selector: 'jhi-religious-belief-delete-dialog',
	templateUrl: './religious-belief-delete-dialog.component.html'
})
export class ReligiousBeliefDeleteDialogComponent {

	religiousBelief: ReligiousBelief;

	constructor(private jhiLanguageService: JhiLanguageService,
				private religiousBeliefService: ReligiousBeliefService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['religiousBelief']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.religiousBeliefService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'religiousBeliefListModification',
				content: 'Deleted an religiousBelief'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-religious-belief-delete-popup',
	template: ''
})
export class ReligiousBeliefDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private religiousBeliefPopupService: ReligiousBeliefPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.religiousBeliefPopupService
				.open(ReligiousBeliefDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
