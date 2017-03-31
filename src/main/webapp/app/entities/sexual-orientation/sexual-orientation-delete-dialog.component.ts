import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {SexualOrientation} from "./sexual-orientation.model";
import {SexualOrientationPopupService} from "./sexual-orientation-popup.service";
import {SexualOrientationService} from "./sexual-orientation.service";

@Component({
	selector: 'jhi-sexual-orientation-delete-dialog',
	templateUrl: './sexual-orientation-delete-dialog.component.html'
})
export class SexualOrientationDeleteDialogComponent {

	sexualOrientation: SexualOrientation;

	constructor(private jhiLanguageService: JhiLanguageService,
				private sexualOrientationService: SexualOrientationService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['sexualOrientation']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.sexualOrientationService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'sexualOrientationListModification',
				content: 'Deleted an sexualOrientation'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-sexual-orientation-delete-popup',
	template: ''
})
export class SexualOrientationDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private sexualOrientationPopupService: SexualOrientationPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.sexualOrientationPopupService
				.open(SexualOrientationDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
