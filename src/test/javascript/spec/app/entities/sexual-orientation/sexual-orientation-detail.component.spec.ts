import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import {MockBackend} from "@angular/http/testing";
import {Http, BaseRequestOptions} from "@angular/http";
import {OnInit} from "@angular/core";
import {DatePipe} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs/Rx";
import {DateUtils, DataUtils, JhiLanguageService} from "ng-jhipster";
import {MockLanguageService} from "../../../helpers/mock-language.service";
import {MockActivatedRoute} from "../../../helpers/mock-route.service";
import {SexualOrientationDetailComponent} from "../../../../../../main/webapp/app/entities/sexual-orientation/sexual-orientation-detail.component";
import {SexualOrientationService} from "../../../../../../main/webapp/app/entities/sexual-orientation/sexual-orientation.service";
import {SexualOrientation} from "../../../../../../main/webapp/app/entities/sexual-orientation/sexual-orientation.model";

describe('Component Tests', () => {

	describe('SexualOrientation Management Detail Component', () => {
		let comp: SexualOrientationDetailComponent;
		let fixture: ComponentFixture<SexualOrientationDetailComponent>;
		let service: SexualOrientationService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [SexualOrientationDetailComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					DateUtils,
					DataUtils,
					DatePipe,
					{
						provide: ActivatedRoute,
						useValue: new MockActivatedRoute({id: 123})
					},
					{
						provide: Http,
						useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
							return new Http(backendInstance, defaultOptions);
						},
						deps: [MockBackend, BaseRequestOptions]
					},
					{
						provide: JhiLanguageService,
						useClass: MockLanguageService
					},
					SexualOrientationService
				]
			}).overrideComponent(SexualOrientationDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(SexualOrientationDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(SexualOrientationService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new SexualOrientation(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.sexualOrientation).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
