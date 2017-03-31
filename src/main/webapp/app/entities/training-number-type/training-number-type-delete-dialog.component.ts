import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {TrainingNumberType} from "./training-number-type.model";
import {TrainingNumberTypePopupService} from "./training-number-type-popup.service";
import {TrainingNumberTypeService} from "./training-number-type.service";

@Component({
	selector: 'jhi-training-number-type-delete-dialog',
	templateUrl: './training-number-type-delete-dialog.component.html'
})
export class TrainingNumberTypeDeleteDialogComponent {

	trainingNumberType: TrainingNumberType;

	constructor(private jhiLanguageService: JhiLanguageService,
				private trainingNumberTypeService: TrainingNumberTypeService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['trainingNumberType']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.trainingNumberTypeService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'trainingNumberTypeListModification',
				content: 'Deleted an trainingNumberType'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-training-number-type-delete-popup',
	template: ''
})
export class TrainingNumberTypeDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private trainingNumberTypePopupService: TrainingNumberTypePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.trainingNumberTypePopupService
				.open(TrainingNumberTypeDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
